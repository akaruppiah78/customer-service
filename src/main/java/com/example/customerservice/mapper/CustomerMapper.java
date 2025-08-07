package com.example.customerservice.mapper;

import com.example.customerservice.dto.CreateCustomerRequest;
import com.example.customerservice.dto.UpdateCustomerRequest;
import com.example.customerservice.dto.CustomerResponse;
import com.example.customerservice.dto.CustomerListResponse.CustomerSummary;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CustomerMapper {
    
    public Customer toEntity(CreateCustomerRequest request) {
        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setAddress(request.address());
        customer.setDateOfBirth(request.dateOfBirth());
        customer.setCustomerStatus(request.customerStatus() != null ? request.customerStatus() : CustomerStatus.ACTIVE);
        
        // Let Spring Data handle the audit fields
        Instant now = Instant.now();
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);
        
        return customer;
    }
    
    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
            customer.getCustomerId(),
            customer.getFirstName(),
            customer.getLastName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getAddress(),
            customer.getDateOfBirth(),
            customer.getCustomerStatus(),
            customer.getCreatedAt(),
            customer.getUpdatedAt()
        );
    }
    
    public CustomerSummary toSummary(Customer customer) {
        return new CustomerSummary(
            customer.getCustomerId(),
            customer.getFirstName(),
            customer.getLastName(),
            customer.getEmail(),
            customer.getCustomerStatus()
        );
    }
    
    public void updateEntityFromRequest(UpdateCustomerRequest request, Customer customer) {
        if (request.firstName() != null) {
            customer.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            customer.setLastName(request.lastName());
        }
        if (request.email() != null) {
            customer.setEmail(request.email());
        }
        if (request.phone() != null) {
            customer.setPhone(request.phone());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
        if (request.dateOfBirth() != null) {
            customer.setDateOfBirth(request.dateOfBirth());
        }
        if (request.customerStatus() != null) {
            customer.setCustomerStatus(request.customerStatus());
        }
        
        // Update the timestamp
        customer.setUpdatedAt(Instant.now());
    }
}
