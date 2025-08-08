package com.example.customerservice.controller;

import com.example.customerservice.dto.ApiResponse;
import com.example.customerservice.dto.CreateCustomerRequest;
import com.example.customerservice.dto.UpdateCustomerRequest;
import com.example.customerservice.dto.CustomerResponse;
import com.example.customerservice.dto.CustomerListResponse;
import com.example.customerservice.model.CustomerStatus;
import com.example.customerservice.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for customer management operations.
 * 
 * <p>This controller provides HTTP endpoints for:</p>
 * <ul>
 *   <li>Creating new customers</li>
 *   <li>Retrieving customers by ID</li>
 *   <li>Updating existing customers</li>
 *   <li>Deleting customers</li>
 *   <li>Listing customers with pagination and filtering</li>
 *   <li>Searching customers by name</li>
 * </ul>
 * 
 * <p>All endpoints return standardized {@link ApiResponse} objects and include
 * comprehensive OpenAPI documentation for automatic API documentation generation.</p>
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer Management", description = "APIs for managing customer information")
public class CustomerController {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    
    private final CustomerService customerService;
    
    /**
     * Constructs a new CustomerController with the required service dependency.
     * 
     * @param customerService the service for customer business operations
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    /**
     * Creates a new customer in the system.
     * 
     * <p>This endpoint validates the request data and creates a new customer record.
     * Email addresses must be unique across all customers.</p>
     * 
     * @param request the customer creation request containing all required customer details
     * @return HTTP 201 with the created customer data, or appropriate error response
     */
    @PostMapping
    @Operation(
        summary = "Create a new customer",
        description = "Creates a new customer record with the provided information"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Customer created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {
        
        logger.info("Received request to create customer with email: {}", request.email());
        
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Customer created successfully", response));
    }
    
    @GetMapping("/{customerId}")
    @Operation(
        summary = "Get customer by ID",
        description = "Retrieves detailed information about a customer by their unique ID"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(
            @Parameter(description = "Customer unique identifier")
            @PathVariable String customerId) {
        
        logger.debug("Received request to get customer with ID: {}", customerId);
        
        CustomerResponse response = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping
    @Operation(
        summary = "Get paginated list of customers",
        description = "Retrieves a paginated list of customers with optional status filtering"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    })
    public ResponseEntity<ApiResponse<CustomerListResponse>> getCustomers(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Number of customers per page (max 1000)")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Filter by customer status")
            @RequestParam(required = false) CustomerStatus status) {
        
        logger.debug("Received request to get customers - page: {}, size: {}, status: {}", page, size, status);
        
        CustomerListResponse response = customerService.getCustomers(page, size, status);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @PutMapping("/{customerId}")
    @Operation(
        summary = "Update customer information",
        description = "Updates customer information. Only provided fields will be updated."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Customer not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @Parameter(description = "Customer unique identifier")
            @PathVariable String customerId,
            
            @Valid @RequestBody UpdateCustomerRequest request) {
        
        logger.info("Received request to update customer with ID: {}", customerId);
        
        CustomerResponse response = customerService.updateCustomer(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", response));
    }
    
    @DeleteMapping("/{customerId}")
    @Operation(
        summary = "Delete customer",
        description = "Deletes a customer record by their unique ID"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<ApiResponse<String>> deleteCustomer(
            @Parameter(description = "Customer unique identifier")
            @PathVariable String customerId) {
        
        logger.info("Received request to delete customer with ID: {}", customerId);
        
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully", customerId));
    }
    
    @GetMapping("/search")
    @Operation(
        summary = "Search customers by name",
        description = "Search customers by first name or last name with pagination"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<ApiResponse<CustomerListResponse>> searchCustomers(
            @Parameter(description = "Name to search for (first or last name)")
            @RequestParam String name,
            
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Number of customers per page (max 1000)")
            @RequestParam(defaultValue = "10") int size) {
        
        logger.debug("Received request to search customers with name: {}", name);
        
        CustomerListResponse response = customerService.searchCustomers(name, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
