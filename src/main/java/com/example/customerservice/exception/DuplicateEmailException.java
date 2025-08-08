package com.example.customerservice.exception;

/**
 * Exception thrown when attempting to create or update a customer with an email
 * address that already exists in the system.
 * 
 * <p>This exception enforces the business rule that email addresses must be unique
 * across all customers. It is thrown in the following scenarios:</p>
 * <ul>
 *   <li>Creating a new customer with an email that already exists</li>
 *   <li>Updating a customer's email to one that belongs to another customer</li>
 * </ul>
 * 
 * <p>The exception is handled by the global exception handler to return
 * appropriate HTTP 409 Conflict responses to API clients.</p>
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 * @see com.example.customerservice.exception.GlobalExceptionHandler
 */
public class DuplicateEmailException extends RuntimeException {
    
    /**
     * Constructs a new DuplicateEmailException with a message indicating
     * which email address is already in use.
     * 
     * @param email the email address that already exists in the system
     */
    public DuplicateEmailException(String email) {
        super("Customer with email '" + email + "' already exists");
    }
}
