package com.example.customerservice.dto;

import java.util.List;

/**
 * Data Transfer Object for paginated customer list responses.
 * 
 * <p>This record encapsulates customer list data along with pagination metadata
 * for efficient handling of large customer datasets. It provides essential
 * pagination information to support client-side navigation and data management.</p>
 * 
 * <p>Response structure includes:</p>
 * <ul>
 *   <li>Customer list with summary information for each customer</li>
 *   <li>Current page information and size configuration</li>
 *   <li>Total count metrics for complete dataset understanding</li>
 *   <li>Navigation flags for previous/next page availability</li>
 * </ul>
 * 
 * @param customers list of customer summary objects for the current page
 * @param page current page number (0-based indexing)
 * @param size number of customers per page
 * @param totalElements total number of customers across all pages
 * @param totalPages total number of pages available
 * @param hasNext indicates if there are more pages after the current one
 * @param hasPrevious indicates if there are pages before the current one
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 */
public record CustomerListResponse(
    List<CustomerSummary> customers,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrevious
) {
    
    /**
     * Simplified customer information for list displays.
     * 
     * <p>This nested record contains essential customer information for list views,
     * excluding detailed information like address, phone, and audit fields to
     * optimize response size and improve performance for list operations.</p>
     * 
     * @param customerId unique identifier for the customer
     * @param firstName customer's first name
     * @param lastName customer's last name
     * @param email customer's email address
     * @param customerStatus current status of the customer account
     * 
     * @author Customer Service Team
     * @version 1.0.0
     * @since 1.0.0
     */
    public record CustomerSummary(
        String customerId,
        String firstName,
        String lastName,
        String email,
        com.example.customerservice.model.CustomerStatus customerStatus
    ) {}
}
