package com.secureflow.secureflow_backend.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI secureFlowAPI(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("SecureFlow API")
                                .description("Enterprise Vulnerability & Incident Management Platform")
                                .version("v1.0")
                );
    }
}
