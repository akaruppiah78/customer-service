package com.example.customerservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for CustomerServiceApplication main class.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/test"
})
class CustomerServiceApplicationTest {

    @Test
    @DisplayName("Should load Spring Boot application context successfully")
    void shouldLoadSpringBootApplicationContextSuccessfully(ApplicationContext context) {
        // Then
        assertThat(context).isNotNull();
    }

    @Test
    @DisplayName("Should have SpringBootApplication annotation")
    void shouldHaveSpringBootApplicationAnnotation() {
        // Given - Check if the class has @SpringBootApplication annotation
        org.springframework.boot.autoconfigure.SpringBootApplication annotation = 
                CustomerServiceApplication.class.getAnnotation(
                        org.springframework.boot.autoconfigure.SpringBootApplication.class);

        // Then
        assertThat(annotation).isNotNull();
    }

    @Test
    @DisplayName("Should have main method")
    void shouldHaveMainMethod() {
        // When - Check if main method exists
        java.lang.reflect.Method mainMethod;
        try {
            mainMethod = CustomerServiceApplication.class.getMethod("main", String[].class);

            // Then
            assertThat(mainMethod).isNotNull();
            assertThat(mainMethod.getReturnType()).isEqualTo(void.class);
            assertThat(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers())).isTrue();
            assertThat(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers())).isTrue();
        } catch (NoSuchMethodException e) {
            throw new AssertionError("main method should exist", e);
        }
    }

    @Test
    @DisplayName("Should create CustomerServiceApplication instance")
    void shouldCreateCustomerServiceApplicationInstance() {
        // When
        CustomerServiceApplication app = new CustomerServiceApplication();

        // Then
        assertThat(app).isNotNull()
                .isInstanceOf(CustomerServiceApplication.class);
    }

    @Test
    @DisplayName("Should be in correct package")
    void shouldBeInCorrectPackage() {
        // Then
        assertThat(CustomerServiceApplication.class.getPackage().getName())
                .isEqualTo("com.example.customerservice");
    }

    @Test
    @DisplayName("Should be a public class")
    void shouldBeAPublicClass() {
        // Then
        assertThat(java.lang.reflect.Modifier.isPublic(CustomerServiceApplication.class.getModifiers()))
                .isTrue();
    }

    @Test
    @DisplayName("Should not be abstract class")
    void shouldNotBeAbstractClass() {
        // Then
        assertThat(java.lang.reflect.Modifier.isAbstract(CustomerServiceApplication.class.getModifiers()))
                .isFalse();
    }

    @Test
    @DisplayName("Should not be final class")
    void shouldNotBeFinalClass() {
        // Then
        assertThat(java.lang.reflect.Modifier.isFinal(CustomerServiceApplication.class.getModifiers()))
                .isFalse();
    }
}
