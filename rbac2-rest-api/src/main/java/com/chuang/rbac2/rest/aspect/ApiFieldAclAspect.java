package com.chuang.rbac2.rest.aspect;

import com.chuang.rbac2.crud.entity.Api;
import com.chuang.rbac2.crud.entity.ApiFieldAcl;
import com.chuang.rbac2.crud.enums.FieldAction;
import com.chuang.rbac2.crud.event.ApiUpdatedEvent;
import com.chuang.rbac2.crud.service.IApiFieldAclService;
import com.chuang.rbac2.crud.service.IApiService;
import com.chuang.rbac2.rest.OfficeUtils;
import com.chuang.rbac2.rest.model.ShiroUser;
import com.chuang.tauceti.support.exception.SystemException;
import com.chuang.tauceti.support.rowquery.RowQuery;
import com.chuang.tauceti.tools.basic.ObjectKit;
import com.chuang.tauceti.tools.basic.StringKit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@Slf4j
public class ApiFieldAclAspect implements ApplicationListener<ApiUpdatedEvent> {
    @Resource private IApiService apiService;
    @Resource private IApiFieldAclService apiFieldAclService;
    @Resource private HttpServletRequest request;

    private Map<String, Api> apiMap = new HashMap<>();
    private Map<String, List<ApiFieldAcl>> apiFieldAclMap = new HashMap<>();

    @Around(value = "(@within(org.springframework.web.bind.annotation.RestController) || @annotation(org.springframework.web.bind.annotation.ResponseBody)) &&" +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("??????api field acl aspect");
        MethodSignature signature = (MethodSignature) point.getSignature();
        Object[] args = handArgs(signature, point.getArgs());
        return handACL(point.proceed(args), false);
    }

    private Object[] handArgs(MethodSignature signature, Object[] pointArgs) {
        Object[] args = Arrays.copyOf(pointArgs, pointArgs.length);

        String code = requestCode();
        Api api = apiMap.get(code);
        if(null == api) {
            return args;
        }

        String[] names = signature.getParameterNames();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            params.put(names[i], args[i]);
        }

        handACL(params, true);

        for (int i = 0; i < names.length; i++) {
            args[i] = params.get(names[i]);
        }

        return args;
    }

    private Object handACL(Object object, boolean income) {
        if (null == object) {
            return null;
        }
        if (isAsync(object)) {
            if(income) {
                throw new SystemException("???????????????????????????");
            } else {
                return handAsyncResult(object);
            }
        } else {
           return handSyncACL(object, income);
        }
    }

    private Object handAsyncResult(Object result) {
        if(result instanceof Mono) {
            return ((Mono<?>) result).map(o -> handSyncACL(o, false));
        }
        if (result instanceof CompletableFuture) {
            return Mono.fromFuture((CompletableFuture<?>) result).map(o -> handSyncACL(o, false));
        }
        throw new SystemException("????????????????????????Mono???CompletableFuture. ????????????????????????[DeferredResult, Callable, Future]?????????????????????");
    }

    private Object handSyncACL(Object object, boolean income) {
        return OfficeUtils.shiroUser().map(user -> handSyncACL(user, object, income)).orElse(object);
    }

    private Object handSyncACL(ShiroUser user, Object object, boolean income) {
        String code = requestCode();
        Api api = apiMap.get(code);
        if(null == api) {
            return object;
        }
        List<ApiFieldAcl> aclList = apiFieldAclMap.get(code);

        for(ApiFieldAcl acl : aclList) {
            if(acl.getIncome() == income && OfficeUtils.canAll(acl.roles(), acl.abilities())) {
                if(object instanceof RowQuery) {
                    aclRowQuery(acl, (RowQuery) object);
                } else {
                    aclObject(acl, object);
                }
            }
        }
        return object;
    }

    private void aclRowQuery(ApiFieldAcl acl, RowQuery object) {
        String field = acl.getFieldEl().replace("@", "").replace(" ", "");
        RowQuery.Filter[] filters = (RowQuery.Filter[])Arrays.stream(object.getFilters())
                .filter(filter -> !field.equals(filter.getField()))
                .toArray();
        object.setFilters(filters);
    }

    private void aclObject(ApiFieldAcl acl, Object object) {
        if (acl.getAction() == FieldAction.HIDDEN) {
            setValue(object, acl.getFieldEl(), o -> null);
        } else if (acl.getAction() == FieldAction.HALF_HIDDEN) {
            setValue(object, acl.getFieldEl(), o -> {
                if (o instanceof String) {
                    return StringKit.halfHidden((String) o);
                } else {
                    log.warn("????????????{}??????string??????????????????????????????", acl.getFieldEl());
                    return o;
                }
            });
        }
    }


    private boolean isAsync(Object result) {
        return result instanceof Callable ||
                result instanceof DeferredResult ||
                result instanceof Mono ||
                result instanceof Future;
    }


    @Override
    public void onApplicationEvent(@Nullable ApiUpdatedEvent event) {
        loadApiConfig();
    }

    private synchronized void loadApiConfig() {
        this.apiMap = apiService.findEnablers().stream()
                .collect(Collectors.toMap(Api::getCode, s -> s));
        this.apiFieldAclMap = apiFieldAclService.list().stream()
                .collect(Collectors.groupingBy(ApiFieldAcl::getApiCode));
    }

    private HttpMethod method() {
        return HttpMethod.valueOf(request.getMethod().toUpperCase());
    }

    private String requestCode() {
        return apiService.genCode(request.getRequestURI(), method().name());
    }


    private void setValue(Object obj, String el, Function<Object, Object> map) {
        setValue0(obj, el.replace("@", "").replace(" ", ""), map);
    }


    private void setValue0(Object obj, String el, Function<Object, Object> map) {
        if(null == obj) {
            return;
        }
        List<String> fields = Arrays.stream(el.split("\\.")).filter(StringKit::isNotBlank).collect(Collectors.toList());
        Stream<?> stream = valueAsStream(obj);
        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            if (i == fields.size() - 1) {
                // ??????????????????
                stream.forEach(o -> {
                    ObjectKit.getValue(o, field).ifPresent(value -> {
                        ObjectKit.setValue(o, field, map.apply(value));
                    });
                });
            } else {
                stream = stream.flatMap(o -> valueAsStream(ObjectKit.getValue(o, field)));
            }
        }
    }

    private Stream<Object> valueAsStream(Object obj) {
        if (obj.getClass().isArray()) {
            return Stream.of((Object[]) obj);
        } else if(obj instanceof Iterable) {
            List<Object> list = new ArrayList<>();
            ((Iterable<?>) obj).forEach(list::add);
            return list.stream();
        } else  {
            return Stream.of(obj);
        }
    }
}
