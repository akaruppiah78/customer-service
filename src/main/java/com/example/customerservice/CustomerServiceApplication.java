package com.example.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Customer Service Microservice.
 * 
 * <p>This Spring Boot application provides a RESTful API for managing customer information.
 * It supports full CRUD operations, search functionality, pagination, and data validation.</p>
 * 
 * <p>Key features include:</p>
 * <ul>
 *   <li>Customer management with CRUD operations</li>
 *   <li>Email uniqueness validation</li>
 *   <li>Phone number format validation (E.164)</li>
 *   <li>Search and pagination capabilities</li>
 *   <li>MongoDB integration with auditing</li>
 *   <li>Comprehensive error handling</li>
 *   <li>OpenAPI/Swagger documentation</li>
 * </ul>
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 */
@SpringBootApplication
public class CustomerServiceApplication {

    /**
     * Main method to start the Customer Service application.
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
