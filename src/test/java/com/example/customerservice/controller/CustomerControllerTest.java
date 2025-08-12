package com.example.customerservice.controller;

import com.example.customerservice.dto.CreateCustomerRequest;
import com.example.customerservice.dto.UpdateCustomerRequest;
import com.example.customerservice.dto.CustomerResponse;
import com.example.customerservice.dto.CustomerListResponse;
import com.example.customerservice.exception.CustomerNotFoundException;
import com.example.customerservice.exception.DuplicateEmailException;
import com.example.customerservice.model.CustomerStatus;
import com.example.customerservice.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private CustomerService customerService;
    
    @Test
    void should_CreateCustomer_When_ValidRequest() throws Exception {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest(
            "John", "Doe", "john.doe@example.com", "+1234567890",
            "123 Main St", LocalDate.of(1990, 1, 1), CustomerStatus.ACTIVE
        );
        
        CustomerResponse response = new CustomerResponse(
            "test-id-123", "John", "Doe", "john.doe@example.com", "+1234567890",
            "123 Main St", LocalDate.of(1990, 1, 1), CustomerStatus.ACTIVE, null, null
        );
        
        when(customerService.createCustomer(any(CreateCustomerRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Customer created successfully"))
            .andExpect(jsonPath("$.data.customerId").value("test-id-123"))
            .andExpect(jsonPath("$.data.firstName").value("John"))
            .andExpect(jsonPath("$.data.lastName").value("Doe"))
            .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }
    
    @Test
    void should_ReturnBadRequest_When_InvalidCreateRequest() throws Exception {
        // Given
        CreateCustomerRequest invalidRequest = new CreateCustomerRequest(
            "", "", "invalid-email", "invalid-phone", null, null, null
        );
        
        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("ERROR"))
            .andExpect(jsonPath("$.message").value("Validation failed"));
    }
    
    @Test
    void should_ReturnConflict_When_EmailAlreadyExists() throws Exception {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest(
            "John", "Doe", "john.doe@example.com", "+1234567890",
            "123 Main St", LocalDate.of(1990, 1, 1), CustomerStatus.ACTIVE
        );
        
        when(customerService.createCustomer(any(CreateCustomerRequest.class)))
            .thenThrow(new DuplicateEmailException("john.doe@example.com"));
        
        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value("ERROR"))
            .andExpect(jsonPath("$.message").value("Customer with email 'john.doe@example.com' already exists"));
    }
    
    @Test
    void should_GetCustomer_When_CustomerExists() throws Exception {
        // Given
        CustomerResponse response = new CustomerResponse(
            "test-id-123", "John", "Doe", "john.doe@example.com", "+1234567890",
            "123 Main St", LocalDate.of(1990, 1, 1), CustomerStatus.ACTIVE, null, null
        );
        
        when(customerService.getCustomerById("test-id-123")).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/api/v1/customers/test-id-123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.data.customerId").value("test-id-123"))
            .andExpect(jsonPath("$.data.firstName").value("John"));
    }
    
    @Test
    void should_ReturnNotFound_When_CustomerDoesNotExist() throws Exception {
        // Given
        when(customerService.getCustomerById("nonexistent-id"))
            .thenThrow(new CustomerNotFoundException("nonexistent-id"));
        
        // When & Then
        mockMvc.perform(get("/api/v1/customers/nonexistent-id"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("ERROR"))
            .andExpect(jsonPath("$.message").value("Customer not found with ID: nonexistent-id"));
    }
    
    @Test
    void should_GetCustomers_When_NoFilters() throws Exception {
        // Given
        CustomerListResponse.CustomerSummary summary = new CustomerListResponse.CustomerSummary(
            "test-id-123", "John", "Doe", "john.doe@example.com", CustomerStatus.ACTIVE
        );
        
        CustomerListResponse response = new CustomerListResponse(
            List.of(summary), 0, 10, 1, 1, false, false
        );
        
        when(customerService.getCustomers(0, 10, null)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/api/v1/customers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.data.customers").isArray())
            .andExpect(jsonPath("$.data.customers[0].customerId").value("test-id-123"))
            .andExpect(jsonPath("$.data.totalElements").value(1));
    }
    
    @Test
    void should_UpdateCustomer_When_ValidRequest() throws Exception {
        // Given
        UpdateCustomerRequest request = new UpdateCustomerRequest(
            "John", "Smith", "john.smith@example.com", "+0987654321",
            "456 Oak Ave", LocalDate.of(1990, 1, 1), CustomerStatus.INACTIVE
        );
        
        CustomerResponse response = new CustomerResponse(
            "test-id-123", "John", "Smith", "john.smith@example.com", "+0987654321",
            "456 Oak Ave", LocalDate.of(1990, 1, 1), CustomerStatus.INACTIVE, null, null
        );
        
        when(customerService.updateCustomer(eq("test-id-123"), any(UpdateCustomerRequest.class)))
            .thenReturn(response);
        
        // When & Then
        mockMvc.perform(put("/api/v1/customers/test-id-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Customer updated successfully"))
            .andExpect(jsonPath("$.data.lastName").value("Smith"));
    }
    
    @Test
    void should_DeleteCustomer_When_CustomerExists() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/customers/test-id-123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Customer deleted successfully"))
            .andExpect(jsonPath("$.data").value("test-id-123"));
    }
    
    @Test
    void should_SearchCustomers_When_ValidName() throws Exception {
        // Given
        CustomerListResponse.CustomerSummary summary = new CustomerListResponse.CustomerSummary(
            "test-id-123", "John", "Doe", "john.doe@example.com", CustomerStatus.ACTIVE
        );
        
        CustomerListResponse response = new CustomerListResponse(
            List.of(summary), 0, 10, 1, 1, false, false
        );
        
        when(customerService.searchCustomers("John", 0, 10)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/api/v1/customers/search?name=John"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.data.customers").isArray())
            .andExpect(jsonPath("$.data.customers[0].firstName").value("John"));
    }
    
    @Test
    void should_ReturnNotFound_When_DeletingNonexistentCustomer() throws Exception {
        // Given
        doThrow(new CustomerNotFoundException("nonexistent-id")).when(customerService).deleteCustomer("nonexistent-id");

        // When & Then
        mockMvc.perform(delete("/api/v1/customers/nonexistent-id"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("ERROR"))
            .andExpect(jsonPath("$.message").value("Customer not found with ID: nonexistent-id"));
    }

    @Test
    void should_ReturnBadRequest_When_InvalidUpdateRequest() throws Exception {
        // Given
        UpdateCustomerRequest invalidRequest = new UpdateCustomerRequest(
            "", "", "invalid-email", "invalid-phone", null, null, null
        );

        // When & Then
        mockMvc.perform(put("/api/v1/customers/test-id-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("ERROR"))
            .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void should_ReturnNotFound_When_UpdatingNonexistentCustomer() throws Exception {
        // Given
        UpdateCustomerRequest request = new UpdateCustomerRequest(
            "John", "Smith", "john.smith@example.com", "+0987654321",
            "456 Oak Ave", LocalDate.of(1990, 1, 1), CustomerStatus.INACTIVE
        );
        when(customerService.updateCustomer(eq("nonexistent-id"), any(UpdateCustomerRequest.class)))
            .thenThrow(new CustomerNotFoundException("nonexistent-id"));

        // When & Then
        mockMvc.perform(put("/api/v1/customers/nonexistent-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value("ERROR"))
            .andExpect(jsonPath("$.message").value("Customer not found with ID: nonexistent-id"));
    }

    @Test
    void should_GetCustomers_WithStatusFilter() throws Exception {
        // Given
        CustomerListResponse.CustomerSummary summary = new CustomerListResponse.CustomerSummary(
            "test-id-123", "John", "Doe", "john.doe@example.com", CustomerStatus.ACTIVE
        );
        CustomerListResponse response = new CustomerListResponse(
            List.of(summary), 0, 10, 1, 1, false, false
        );
        when(customerService.getCustomers(0, 10, CustomerStatus.ACTIVE)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/customers?status=ACTIVE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.data.customers").isArray())
            .andExpect(jsonPath("$.data.customers[0].customerId").value("test-id-123"))
            .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    void should_SearchCustomers_ReturnEmpty_When_NoResults() throws Exception {
        // Given
        CustomerListResponse response = new CustomerListResponse(
            List.of(), 0, 10, 0, 0, false, false
        );
        when(customerService.searchCustomers("Jane", 0, 10)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/customers/search?name=Jane"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.data.customers").isArray())
            .andExpect(jsonPath("$.data.customers").isEmpty());
    }
}
