package com.example.customerservice.exception;

/**
 * Exception thrown when a requested customer cannot be found in the system.
 * 
 * <p>This exception is typically thrown when:</p>
 * <ul>
 *   <li>Attempting to retrieve a customer by ID that doesn't exist</li>
 *   <li>Trying to update or delete a customer that has been removed</li>
 *   <li>Accessing a customer that may have been archived or soft-deleted</li>
 * </ul>
 * 
 * <p>The exception is handled by the global exception handler to return
 * appropriate HTTP 404 responses to API clients.</p>
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 * @see com.example.customerservice.exception.GlobalExceptionHandler
 */
public class CustomerNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new CustomerNotFoundException with a message indicating
     * which customer ID was not found.
     * 
     * @param customerId the ID of the customer that could not be found
     */
    public CustomerNotFoundException(String customerId) {
        super("Customer not found with ID: " + customerId);
    }
}
