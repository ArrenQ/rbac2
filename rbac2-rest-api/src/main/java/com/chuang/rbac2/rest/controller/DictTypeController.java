package com.chuang.rbac2.rest.controller;

import com.chuang.rbac2.crud.entity.DictType;
import com.chuang.rbac2.crud.service.IDictTypeService;
import com.chuang.rbac2.rest.model.co.DictTypeCO;
import com.chuang.rbac2.rest.model.ro.DictTypeRO;
import com.chuang.rbac2.rest.model.uo.DictTypeUO;
import com.chuang.tauceti.rowquery.controller.basic.ICrudController;
import com.chuang.tauceti.support.Result;
import com.chuang.tauceti.tools.basic.StringKit;
import com.chuang.tauceti.tools.basic.reflect.ConvertKit;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 字典类型; 前端控制器
 * </p>
 *
 * @author chuang
 * @since 2021-05-11
 */
@RestController
@RequestMapping("/dict/type")
public class DictTypeController implements ICrudController<DictTypeCO, DictTypeRO, DictTypeUO, DictType, IDictTypeService> {

    @Resource private IDictTypeService service;

    @GetMapping("/all")
    @RequiresPermissions("dict:read")
    public Result<List<DictTypeRO>> all(String like) {
        if(StringKit.isBlank(like)) {
            return Result.success(service.lambdaQuery().eq(DictType::getEnabled, true).list())
                    .map(configs -> ConvertKit.toBeans(configs, DictTypeRO::new));
        } else {
            return Result.success(service.lambdaQuery().eq(DictType::getEnabled, true).likeLeft(DictType::getCode, like).list())
                    .map(configs -> ConvertKit.toBeans(configs, DictTypeRO::new));
        }
    }

    @Override
    public IDictTypeService service() {
        return service;
    }

    @Override
    public String basePermission() {
        return "dict:";
    }

}

