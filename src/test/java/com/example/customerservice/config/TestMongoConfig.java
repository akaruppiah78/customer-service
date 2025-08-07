package com.example.customerservice.config;

import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
@ConditionalOnClass(EmbeddedMongoAutoConfiguration.class)
@AutoConfigureBefore({MongoAutoConfiguration.class})
public class TestMongoConfig {

    @Bean
    public Version.Main mongoVersion() {
        return Version.Main.V7_0;
    }
}
