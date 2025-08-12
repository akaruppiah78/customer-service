package com.example.customerservice.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for MongoConfig configuration class.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/test"
})
class MongoConfigTest {

    @Autowired
    private MongoConfig mongoConfig;

    @Test
    @DisplayName("Should create MongoConfig bean")
    void shouldCreateMongoConfigBean() {
        // Then
        assertThat(mongoConfig).isNotNull();
    }

    @Test
    @DisplayName("Should have MongoAuditing enabled")
    void shouldHaveMongoAuditingEnabled() {
        // Given - Check if the class has @EnableMongoAuditing annotation
        EnableMongoAuditing annotation = MongoConfig.class.getAnnotation(EnableMongoAuditing.class);

        // Then
        assertThat(annotation).isNotNull();
    }

    @Test
    @DisplayName("Should be a configuration class")
    void shouldBeAConfigurationClass() {
        // Given - Check if the class has @Configuration annotation
        org.springframework.context.annotation.Configuration annotation = 
                MongoConfig.class.getAnnotation(org.springframework.context.annotation.Configuration.class);

        // Then
        assertThat(annotation).isNotNull();
    }

    @Test
    @DisplayName("Should create different instances when requested")
    void shouldCreateDifferentInstancesWhenRequested() {
        // When
        MongoConfig config1 = new MongoConfig();
        MongoConfig config2 = new MongoConfig();

        // Then
        assertThat(config1).isNotNull();
        assertThat(config2).isNotNull();
        assertThat(config1).isNotSameAs(config2);
    }
}
