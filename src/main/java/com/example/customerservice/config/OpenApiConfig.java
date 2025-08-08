package com.example.customerservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for generating API documentation.
 * 
 * <p>This configuration sets up Swagger/OpenAPI documentation for the Customer Service API.
 * The generated documentation includes:</p>
 * <ul>
 *   <li>API metadata (title, description, version)</li>
 *   <li>Contact information for the development team</li>
 *   <li>License information</li>
 *   <li>Interactive API explorer (Swagger UI)</li>
 * </ul>
 * 
 * <p>The documentation is accessible at:</p>
 * <ul>
 *   <li>Swagger UI: http://localhost:8080/swagger-ui/index.html</li>
 *   <li>OpenAPI JSON: http://localhost:8080/v3/api-docs</li>
 * </ul>
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
public class OpenApiConfig {
    
    /**
     * Creates the OpenAPI specification for the Customer Service API.
     * 
     * @return configured OpenAPI object with API metadata and documentation
     */
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
