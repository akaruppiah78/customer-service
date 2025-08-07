package com.example.customerservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customerServiceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Customer Service API")
                .description("RESTful API for managing customer information")
                .version("v1.0.0")
                .contact(new Contact()
                    .name("Development Team")
                    .email("dev@example.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")));
    }
}
