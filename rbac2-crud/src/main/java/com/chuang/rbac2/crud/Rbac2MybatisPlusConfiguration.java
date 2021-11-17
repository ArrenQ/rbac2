package com.chuang.rbac2.crud;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chuang.tauceti.tools.basic.StringKit;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 这个配置的目的是将项目中引用到的枚举和entity自动加入到配置中。
 * @author ATH
 */
@Configuration
@ComponentScan("com.chuang.rbac2.crud")
@MapperScan(
        basePackages= "com.chuang.rbac2.crud",
        markerInterface = BaseMapper.class
)
public class Rbac2MybatisPlusConfiguration {

    public static final String RBAC_ENUM_PACKAGE = "com.chuang.rbac2.crud.enums";
    public static final String TAUCETI_ENUM_PACKAGE = "com.chuang.tauceti.support.enums";
    public static final String RBAC_ALIASES_PACKAGE = "com.chuang.rbac2.crud.entity";


    @Bean
    public MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer() {
        return this::handProperties;
    }

    private void handProperties(MybatisPlusProperties properties) {
        // 默认将 RBAC_ENUM_PACKAGE 和 TAUCETI_ENUM_PACKAGE 添加到配置中
        if (StringKit.isBlank(properties.getTypeEnumsPackage())) {
            properties.setTypeEnumsPackage(RBAC_ENUM_PACKAGE + "," + TAUCETI_ENUM_PACKAGE);
        } else {
            List<String> typeEnumsPackage = Arrays.stream(properties.getTypeEnumsPackage().split(","))
                    .map(String::trim)
                    .filter(s -> !s.equals(RBAC_ENUM_PACKAGE) && !s.equals(TAUCETI_ENUM_PACKAGE))
                    .collect(Collectors.toList());

            properties.setTypeEnumsPackage(StringKit.join(typeEnumsPackage, ",") + "," + RBAC_ENUM_PACKAGE + "," + TAUCETI_ENUM_PACKAGE);
        }

        // 默认将 RBAC_ALIASES_PACKAGE 添加到配置中
        if (StringKit.isBlank(properties.getTypeAliasesPackage())) {
            properties.setTypeAliasesPackage(RBAC_ALIASES_PACKAGE);
        } else {
            List<String> typeAliasesPackage = Arrays.stream(properties.getTypeEnumsPackage().split(","))
                    .map(String::trim)
                    .filter(s -> !s.equals(RBAC_ALIASES_PACKAGE))
                    .collect(Collectors.toList());
            properties.setTypeAliasesPackage(StringKit.join(typeAliasesPackage, ",") + "," + RBAC_ALIASES_PACKAGE);
        }
    }
}
