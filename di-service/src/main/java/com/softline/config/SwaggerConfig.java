package com.softline.config;

import com.softline.common.config.BaseSwaggerConfig;
import com.softline.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * Created by dong on 2020/11/25.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.softline.controller")
                .title("data-integration-system")
                .description("后台相关接口文档")
                .contactName("softline")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
