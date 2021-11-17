package com.chuang.rbac2.rest;

import com.chuang.rbac2.crud.Rbac2CrudConfiguration;
import com.chuang.rbac2.rest.aspect.PageOrRestInterceptor;
import com.chuang.rbac2.rest.model.ShiroUser;
import com.chuang.tauceti.rowquery.RowQueryConverter;
import com.chuang.tauceti.rowquery.handlers.AutoTimeHandler;
import com.chuang.tauceti.shiro.spring.web.jwt.realm.IShiroService;
import com.chuang.tauceti.shiro.spring.web.jwt.realm.LoginRealm;
import com.chuang.tauceti.support.ValueGetter;
import com.chuang.tauceti.tools.third.jackson.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@Import(Rbac2CrudConfiguration.class)
@ComponentScan("com.chuang.rbac2.rest")
@EnableSwagger2
public class Rbac2RestConfiguration implements WebMvcConfigurer {

    /**
     * 覆盖 {@link com.chuang.tauceti.shiro.spring.web.jwt.configuration.ShiroJwtConfiguration}的实现
     * 目的是为了给用户的授权信息做缓存
     */
    @Bean
    @ConditionalOnMissingBean
    public LoginRealm loginRealm(IShiroService shiroService, HashedCredentialsMatcher hashedCredentialsMatcher) {
        LoginRealm realm = new LoginRealm(shiroService);

        /*
         * 可以通过  realm.setAuthenticationCache()和 setCacheManager来设置缓存，后者是lazy创建
         * 脚手架得简单，默认采用shiro内置的内存缓存，其实完全是够用的。如果有特别需要可以自行更改为redis实现。
         */
        realm.setCacheManager(new MemoryConstrainedCacheManager());
        realm.setAuthorizationCachingEnabled(true);
        realm.setCredentialsMatcher(hashedCredentialsMatcher);
        return realm;
    }


    @Bean("allApisSwagger")
    @ConditionalOnMissingBean
    public Docket allApisSwagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("所有")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.chuang"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("服务端接口文档")
                .description("所有/仅CRUD模块/除CRUD以外模块的文档")
                .termsOfServiceUrl("http://localhost:8280")
                .version("1.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    @ConditionalOnMissingBean
    public AutoTimeHandler autoTimeHandler(ValueGetter<String> operatorManager) {
        return new AutoTimeHandler(operatorManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public ValueGetter<String> operatorManager() {
        return ValueGetter.createOpt(() -> OfficeUtils.shiroUser().map(ShiroUser::getUsername));
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new RowQueryConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PageOrRestInterceptor());
    }



    @Bean
    @Order
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

        return jacksonObjectMapperBuilder -> {
            // 为 String 类型自定义反序列化操作
            jacksonObjectMapperBuilder
                    .deserializerByType(String.class, new StdScalarDeserializer<Object>(String.class) {
                        @Override
                        public String deserialize(JsonParser jsonParser, DeserializationContext ctx)
                                throws IOException {
                            // 去除前后空格
                            return StringUtils.trimWhitespace(jsonParser.getValueAsString());
                        }
                    })
                    .deserializerByType(LocalDateTime.class, new LocalDateDeserializer())
                    .deserializerByType(LocalDate.class, new LocalDateTimeDeserializer())
                    .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        };
    }

}
