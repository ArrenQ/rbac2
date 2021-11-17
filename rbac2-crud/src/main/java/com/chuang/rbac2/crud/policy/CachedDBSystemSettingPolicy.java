package com.chuang.rbac2.crud.policy;

import com.chuang.rbac2.crud.Rbac2CrudProperties;
import com.chuang.rbac2.crud.entity.Config;
import com.chuang.rbac2.crud.service.IConfigService;
import com.chuang.tauceti.tools.basic.ScheduleKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author ath
 */
@Service
@Slf4j
public class CachedDBSystemSettingPolicy implements SystemConfigPolicy {

    private final IConfigService configService;
    private final ConcurrentHashMap<String, String> cached = new ConcurrentHashMap<>();
    private final Rbac2CrudProperties rbac2CrudProperties;

    public CachedDBSystemSettingPolicy(IConfigService configService, Rbac2CrudProperties rbac2CrudProperties) {
        this.configService = configService;
        this.rbac2CrudProperties = rbac2CrudProperties;
    }

    @PostConstruct()
    public void init() {
        ScheduleKit.schedule(this::clearCached, rbac2CrudProperties.getClearSettingCacheTime(), TimeUnit.SECONDS);
    }

    @Override
    public boolean contains(String key) {
        if(cached.containsKey(key)) {
            return true;
        }
        return get(key).isPresent();
    }

    @Override
    public void clearCached() {
        log.info("============= 清空配置缓存 ===========");
        cached.clear();
        ScheduleKit.schedule(this::clearCached, rbac2CrudProperties.getClearSettingCacheTime(), TimeUnit.SECONDS);
    }


    @Override
    public Optional<String> get(String key) {
        if(cached.containsKey(key)) {
            return Optional.of(cached.get(key));
        }

        Optional<String> value = configService.findByCode(key).map(Config::getValue);
        value.ifPresent(v -> cached.put(key, v));
        return value;
    }

    @Override
    public void set(String key, String value, String regex) {
        Config sp = configService.lambdaQuery().eq(Config::getCode, key).one();
        Config entity = new Config();
        if (null == sp) {
            entity.setValue(value);
            configService.updateById(entity);
        } else {
            entity.setCode(key);
            entity.setValue(value);
            entity.setValueRegex(regex);
            configService.save(entity);
        }
        cached.put(key, value);
    }
}
