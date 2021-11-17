package com.chuang.rbac2.crud;

import com.chuang.tauceti.rowquery.handlers.AutoTimeHandler;
import com.chuang.tauceti.support.ValueGetter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Rbac2MybatisPlusConfiguration.class)
@ComponentScan("com.chuang.rbac2.crud")
public class Rbac2CrudConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AutoTimeHandler autoTimeHandler(ValueGetter<String> operatorManager) {
        return new AutoTimeHandler(operatorManager);
    }

}
