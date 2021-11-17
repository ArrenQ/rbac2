package com.chuang.rbac2.rest;

import com.chuang.rbac2.crud.Rbac2AutoConfiguration;
import com.chuang.tauceti.shiro.spring.web.jwt.configuration.ShiroJwtAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Rbac2RestConfiguration.class)
@AutoConfigureBefore({ShiroJwtAutoConfiguration.class, Rbac2AutoConfiguration.class})
public class Rbac2RestAutoConfiguration {
}
