package com.example.customerservice.model;

/**
 * Enumeration representing the possible statuses of a customer account.
 * 
 * <p>Customer status is used to control access and behavior of customer accounts:</p>
 * <ul>
 *   <li><strong>ACTIVE</strong>: Customer account is active and fully functional</li>
 *   <li><strong>INACTIVE</strong>: Customer account is temporarily disabled</li>
 *   <li><strong>SUSPENDED</strong>: Customer account is suspended due to policy violations</li>
 * </ul>
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 */
public enum CustomerStatus {
    
    /**
     * Customer account is active and fully functional.
     * This is the default status for new customers.
     */
    ACTIVE,
    
    /**
     * Customer account is temporarily disabled.
     * May be reactivated by customer request or administrative action.
     */
    INACTIVE,
    
    /**
     * Customer account is suspended due to policy violations.
     * Requires administrative review before reactivation.
     */
    SUSPENDED
}
