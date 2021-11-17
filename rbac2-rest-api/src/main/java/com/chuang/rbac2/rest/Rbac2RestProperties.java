package com.chuang.rbac2.rest;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ATH
 */
@Data
@Component
@ConfigurationProperties(prefix = "rbac2.rest")
public class Rbac2RestProperties {
    private String appName = "RBAC2";
    private String appDescription = "这是一个 RBAC2 权限框架";
    private boolean dbMessageSource = true;
}
