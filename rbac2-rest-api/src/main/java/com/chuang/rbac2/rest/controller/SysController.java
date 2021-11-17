package com.chuang.rbac2.rest.controller;

import com.chuang.rbac2.rest.Rbac2RestProperties;
import com.chuang.rbac2.rest.model.ro.AppRO;
import com.chuang.tauceti.support.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ath
 */
@RequestMapping("/sys")
@RestController
public class SysController {

    @Resource private Rbac2RestProperties properties;

    @GetMapping("/app-info")
    public Result<AppRO> appInfo() {
        AppRO app = new AppRO();
        app.setDescription(properties.getAppDescription());
        app.setName(properties.getAppName());
        return Result.success(app);
    }
}
