package com.example.customerservice.repository;

import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for customer data access operations.
 * 
 * <p>This repository extends Spring Data MongoDB's {@link MongoRepository} to provide
 * CRUD operations and custom query methods for {@link Customer} entities.</p>
 * 
 * <p>Custom methods include:</p>
 * <ul>
 *   <li>Finding customers by email address</li>
 *   <li>Checking email existence for uniqueness validation</li>
 *   <li>Filtering customers by status</li>
 *   <li>Searching customers by name with case-insensitive matching</li>
 * </ul>
 * 
 * <p>All query methods support pagination and are automatically implemented by
 * Spring Data MongoDB based on method naming conventions.</p>
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    
    /**
     * Finds a customer by their email address.
     * 
     * @param email the email address to search for
     * @return an Optional containing the customer if found, empty otherwise
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Checks if a customer exists with the given email address.
     * 
     * @param email the email address to check
     * @return true if a customer with this email exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Checks if a customer exists with the given email address, excluding a specific customer ID.
     * This is useful for validation during customer updates.
     * 
     * @param email the email address to check
     * @param customerId the customer ID to exclude from the check
     * @return true if another customer with this email exists, false otherwise
     */
    boolean existsByEmailAndCustomerIdNot(String email, String customerId);
    
    /**
     * Finds customers by their status with pagination support.
     * 
     * @param customerStatus the status to filter by
     * @param pageable pagination information
     * @return a page of customers matching the specified status
     */
    Page<Customer> findByCustomerStatus(CustomerStatus customerStatus, Pageable pageable);
    
    /**
     * Searches customers by first name or last name with case-insensitive partial matching.
     * 
     * @param firstName the first name pattern to search for
     * @param lastName the last name pattern to search for
     * @param pageable pagination information
     * @return a page of customers whose first or last name contains the search terms
     */
    Page<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstName, String lastName, Pageable pageable);
}
