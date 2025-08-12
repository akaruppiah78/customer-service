package com.example.customerservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for OpenApiConfig configuration class.
 */
class OpenApiConfigTest {

    private OpenApiConfig openApiConfig;

    @BeforeEach
    void setUp() {
        openApiConfig = new OpenApiConfig();
    }

    @Test
    @DisplayName("Should create OpenAPI bean with correct configuration")
    void shouldCreateOpenApiBeanWithCorrectConfiguration() {
        // When
        OpenAPI openAPI = openApiConfig.customerServiceOpenAPI();

        // Then
        assertThat(openAPI).isNotNull();
        
        Info info = openAPI.getInfo();
        assertThat(info).isNotNull();
        assertThat(info.getTitle()).isEqualTo("Customer Service API");
        assertThat(info.getDescription()).isEqualTo("RESTful API for managing customer information");
        assertThat(info.getVersion()).isEqualTo("v1.0.0");
    }

    @Test
    @DisplayName("Should create OpenAPI bean with contact information")
    void shouldCreateOpenApiBeanWithContactInformation() {
        // When
        OpenAPI openAPI = openApiConfig.customerServiceOpenAPI();

        // Then
        Info info = openAPI.getInfo();
        Contact contact = info.getContact();
        
        assertThat(contact).isNotNull();
        assertThat(contact.getName()).isEqualTo("Development Team");
        assertThat(contact.getEmail()).isEqualTo("dev@example.com");
    }

    @Test
    @DisplayName("Should have Configuration annotation")
    void shouldHaveConfigurationAnnotation() {
        // Given - Check if the class has @Configuration annotation
        org.springframework.context.annotation.Configuration annotation = 
                OpenApiConfig.class.getAnnotation(org.springframework.context.annotation.Configuration.class);

        // Then
        assertThat(annotation).isNotNull();
    }

    @Test
    @DisplayName("Should create separate OpenAPI instances")
    void shouldCreateSeparateOpenApiInstances() {
        // When
        OpenAPI openAPI1 = openApiConfig.customerServiceOpenAPI();
        OpenAPI openAPI2 = openApiConfig.customerServiceOpenAPI();

        // Then
        assertThat(openAPI1).isNotNull();
        assertThat(openAPI2).isNotNull();
        assertThat(openAPI1).isNotSameAs(openAPI2); // Different instances since no singleton scope
    }

    @Test
    @DisplayName("Should create OpenAPI with consistent configuration across instances")
    void shouldCreateOpenApiWithConsistentConfigurationAcrossInstances() {
        // When
        OpenAPI openAPI1 = openApiConfig.customerServiceOpenAPI();
        OpenAPI openAPI2 = openApiConfig.customerServiceOpenAPI();

        // Then
        assertThat(openAPI1.getInfo().getTitle()).isEqualTo(openAPI2.getInfo().getTitle());
        assertThat(openAPI1.getInfo().getVersion()).isEqualTo(openAPI2.getInfo().getVersion());
        assertThat(openAPI1.getInfo().getDescription()).isEqualTo(openAPI2.getInfo().getDescription());
        assertThat(openAPI1.getInfo().getContact().getName()).isEqualTo(openAPI2.getInfo().getContact().getName());
        assertThat(openAPI1.getInfo().getContact().getEmail()).isEqualTo(openAPI2.getInfo().getContact().getEmail());
    }

    @Test
    @DisplayName("Should create OpenAPI bean method")
    void shouldCreateOpenApiBeanMethod() {
        // When - Check if the method has @Bean annotation
        java.lang.reflect.Method method;
        try {
            method = OpenApiConfig.class.getMethod("customerServiceOpenAPI");
            org.springframework.context.annotation.Bean beanAnnotation = 
                    method.getAnnotation(org.springframework.context.annotation.Bean.class);

            // Then
            assertThat(beanAnnotation).isNotNull();
        } catch (NoSuchMethodException e) {
            throw new AssertionError("customerServiceOpenAPI method should exist", e);
        }
    }

    @Test
    @DisplayName("Should create OpenApiConfig instance")
    void shouldCreateOpenApiConfigInstance() {
        // When
        OpenApiConfig config = new OpenApiConfig();

        // Then
        assertThat(config).isNotNull()
                .isInstanceOf(OpenApiConfig.class);
    }

    @Test
    @DisplayName("Should verify OpenAPI description contains key phrases")
    void shouldVerifyOpenApiDescriptionContainsKeyPhrases() {
        // When
        OpenAPI openAPI = openApiConfig.customerServiceOpenAPI();

        // Then
        String description = openAPI.getInfo().getDescription();
        assertThat(description).isEqualTo("RESTful API for managing customer information")
                .containsIgnoringCase("customer")
                .containsIgnoringCase("managing")
                .containsIgnoringCase("RESTful");
    }
}
