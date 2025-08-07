package com.example.customerservice.exception;

public class CustomerNotFoundException extends RuntimeException {
    
    public CustomerNotFoundException(String customerId) {
        super("Customer not found with ID: " + customerId);
    }
}
