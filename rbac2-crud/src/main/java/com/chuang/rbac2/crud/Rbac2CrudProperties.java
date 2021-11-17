package com.chuang.rbac2.crud;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rbac2.crud")
public class Rbac2CrudProperties {

    private int clearSettingCacheTime = 10;
}
