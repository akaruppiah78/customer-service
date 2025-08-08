package com.example.customerservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * MongoDB configuration class for the Customer Service application.
 * 
 * <p>This configuration enables MongoDB auditing features, which automatically
 * populate audit fields in entities:</p>
 * <ul>
 *   <li>@CreatedDate fields are set when an entity is first saved</li>
 *   <li>@LastModifiedDate fields are updated whenever an entity is modified</li>
 * </ul>
 * 
 * <p>The auditing works in conjunction with the {@link Customer} entity's
 * createdAt and updatedAt fields to provide automatic timestamp tracking.</p>
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 * @see org.springframework.data.mongodb.config.EnableMongoAuditing
 * @see com.example.customerservice.model.Customer
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
    // MongoAuditing enables automatic population of @CreatedDate and @LastModifiedDate fields
}
