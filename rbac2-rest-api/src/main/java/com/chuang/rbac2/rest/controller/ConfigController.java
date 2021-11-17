package com.chuang.rbac2.rest.controller;

import com.chuang.rbac2.crud.entity.Config;
import com.chuang.rbac2.crud.service.IConfigService;
import com.chuang.rbac2.rest.model.co.ConfigCO;
import com.chuang.rbac2.rest.model.ro.ConfigRO;
import com.chuang.rbac2.rest.model.uo.ConfigUO;
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
 * 系统配置表  前端控制器
 * </p>
 *
 * @author chuang
 * @since 2021-05-11
 */
@RestController
@RequestMapping("/config")
public class ConfigController implements ICrudController<ConfigCO, ConfigRO, ConfigUO, Config, IConfigService> {

    @Resource private IConfigService service;

    @GetMapping("/all")
    @RequiresPermissions("sys-config:query")
    public Result<List<ConfigRO>> all(String like) {
        if(StringKit.isBlank(like)) {
            return Result.success(service.list())
                    .map(configs -> ConvertKit.toBeans(configs, ConfigRO::new));
        } else {
            return Result.success(service.lambdaQuery().likeLeft(Config::getCode, like).list())
                    .map(configs -> ConvertKit.toBeans(configs, ConfigRO::new));
        }
    }

    @Override
    public IConfigService service() {
        return service;
    }

    @Override
    public String basePermission() {
        return "sys-config:";
    }

}

