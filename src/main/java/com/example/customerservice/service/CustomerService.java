package com.example.customerservice.service;

import com.example.customerservice.dto.CreateCustomerRequest;
import com.example.customerservice.dto.UpdateCustomerRequest;
import com.example.customerservice.dto.CustomerResponse;
import com.example.customerservice.dto.CustomerListResponse;
import com.example.customerservice.exception.CustomerNotFoundException;
import com.example.customerservice.exception.DuplicateEmailException;
import com.example.customerservice.mapper.CustomerMapper;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerStatus;
import com.example.customerservice.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CustomerService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }
    
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        logger.info("Creating customer with email: {}", request.email());
        
        // Check for duplicate email
        if (customerRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
        }
        
        // Convert DTO to entity
        Customer customer = customerMapper.toEntity(request);
        customer.setCustomerId(UUID.randomUUID().toString());
        
        // Save customer
        Customer savedCustomer = customerRepository.save(customer);
        logger.info("Customer created successfully with ID: {}", savedCustomer.getCustomerId());
        
        return customerMapper.toResponse(savedCustomer);
    }
    
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(String customerId) {
        logger.debug("Fetching customer with ID: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));
        
        return customerMapper.toResponse(customer);
    }
    
    @Transactional(readOnly = true)
    public CustomerListResponse getCustomers(int page, int size, CustomerStatus status) {
        logger.debug("Fetching customers - page: {}, size: {}, status: {}", page, size, status);
        
        // Validate page parameters
        if (page < 0) page = 0;
        if (size <= 0 || size > 1000) size = 10;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Customer> customerPage;
        if (status != null) {
            customerPage = customerRepository.findByCustomerStatus(status, pageable);
        } else {
            customerPage = customerRepository.findAll(pageable);
        }
        
        return new CustomerListResponse(
            customerPage.getContent().stream()
                .map(customerMapper::toSummary)
                .toList(),
            customerPage.getNumber(),
            customerPage.getSize(),
            customerPage.getTotalElements(),
            customerPage.getTotalPages(),
            customerPage.hasNext(),
            customerPage.hasPrevious()
        );
    }
    
    public CustomerResponse updateCustomer(String customerId, UpdateCustomerRequest request) {
        logger.info("Updating customer with ID: {}", customerId);
        
        Customer existingCustomer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));
        
        // Check for duplicate email if email is being updated
        if (request.email() != null && !request.email().equals(existingCustomer.getEmail())) {
            if (customerRepository.existsByEmailAndCustomerIdNot(request.email(), customerId)) {
                throw new DuplicateEmailException(request.email());
            }
        }
        
        // Update entity from request
        customerMapper.updateEntityFromRequest(request, existingCustomer);
        
        // Save updated customer
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        logger.info("Customer updated successfully with ID: {}", updatedCustomer.getCustomerId());
        
        return customerMapper.toResponse(updatedCustomer);
    }
    
    public void deleteCustomer(String customerId) {
        logger.info("Deleting customer with ID: {}", customerId);
        
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
        
        customerRepository.deleteById(customerId);
        logger.info("Customer deleted successfully with ID: {}", customerId);
    }
    
    @Transactional(readOnly = true)
    public CustomerListResponse searchCustomers(String name, int page, int size) {
        logger.debug("Searching customers with name: {}, page: {}, size: {}", name, page, size);
        
        // Validate page parameters
        if (page < 0) page = 0;
        if (size <= 0 || size > 1000) size = 10;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Customer> customerPage = customerRepository
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name, pageable);
        
        return new CustomerListResponse(
            customerPage.getContent().stream()
                .map(customerMapper::toSummary)
                .toList(),
            customerPage.getNumber(),
            customerPage.getSize(),
            customerPage.getTotalElements(),
            customerPage.getTotalPages(),
            customerPage.hasNext(),
            customerPage.hasPrevious()
        );
    }
}
