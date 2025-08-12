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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    
    @Mock
    private CustomerRepository customerRepository;
    
    @Mock
    private CustomerMapper customerMapper;
    
    @InjectMocks
    private CustomerService customerService;
    
    private CreateCustomerRequest createRequest;
    private UpdateCustomerRequest updateRequest;
    private Customer customer;
    private CustomerResponse customerResponse;
    
    @BeforeEach
    void setUp() {
        createRequest = new CreateCustomerRequest(
            "John", "Doe", "john.doe@example.com", "+1234567890",
            "123 Main St", LocalDate.of(1990, 1, 1), CustomerStatus.ACTIVE
        );
        
        updateRequest = new UpdateCustomerRequest(
            "John", "Smith", "john.smith@example.com", "+0987654321",
            "456 Oak Ave", LocalDate.of(1990, 1, 1), CustomerStatus.INACTIVE
        );
        
        customer = new Customer();
        customer.setCustomerId("test-id-123");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("+1234567890");
        customer.setCustomerStatus(CustomerStatus.ACTIVE);
        
        customerResponse = new CustomerResponse(
            "test-id-123", "John", "Doe", "john.doe@example.com", "+1234567890",
            "123 Main St", LocalDate.of(1990, 1, 1), CustomerStatus.ACTIVE, null, null
        );
    }
    
    @Test
    void should_CreateCustomer_When_EmailIsUnique() {
        // Given
        when(customerRepository.existsByEmail(createRequest.email())).thenReturn(false);
        when(customerMapper.toEntity(createRequest)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponse(customer)).thenReturn(customerResponse);
        
        // When
        CustomerResponse result = customerService.createCustomer(createRequest);
        
        // Then
        assertThat(result).isEqualTo(customerResponse);
        verify(customerRepository).existsByEmail(createRequest.email());
        verify(customerRepository).save(any(Customer.class));
    }
    
    @Test
    void should_ThrowDuplicateEmailException_When_EmailAlreadyExists() {
        // Given
        when(customerRepository.existsByEmail(createRequest.email())).thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> customerService.createCustomer(createRequest))
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessageContaining(createRequest.email());
        
        verify(customerRepository, never()).save(any(Customer.class));
    }
    
    @Test
    void should_GetCustomerById_When_CustomerExists() {
        // Given
        when(customerRepository.findById("test-id-123")).thenReturn(Optional.of(customer));
        when(customerMapper.toResponse(customer)).thenReturn(customerResponse);
        
        // When
        CustomerResponse result = customerService.getCustomerById("test-id-123");
        
        // Then
        assertThat(result).isEqualTo(customerResponse);
        verify(customerRepository).findById("test-id-123");
    }
    
    @Test
    void should_ThrowCustomerNotFoundException_When_CustomerDoesNotExist() {
        // Given
        when(customerRepository.findById("nonexistent-id")).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> customerService.getCustomerById("nonexistent-id"))
            .isInstanceOf(CustomerNotFoundException.class)
            .hasMessageContaining("nonexistent-id");
    }
    
    @Test
    void should_GetCustomers_When_NoStatusFilter() {
        // Given
        List<Customer> customers = List.of(customer);
        Page<Customer> customerPage = new PageImpl<>(customers, PageRequest.of(0, 10), 1);
        CustomerListResponse.CustomerSummary summary = new CustomerListResponse.CustomerSummary(
            "test-id-123", "John", "Doe", "john.doe@example.com", CustomerStatus.ACTIVE
        );
        
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(customerPage);
        when(customerMapper.toSummary(customer)).thenReturn(summary);
        
        // When
        CustomerListResponse result = customerService.getCustomers(0, 10, null);
        
        // Then
        assertThat(result.customers()).hasSize(1);
    assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.page()).isZero();
        assertThat(result.size()).isEqualTo(10);
    }
    
    @Test
    void should_GetCustomers_When_StatusFilterIsApplied() {
        // Given
        List<Customer> customers = List.of(customer);
        Page<Customer> customerPage = new PageImpl<>(customers, PageRequest.of(0, 10), 1);
        CustomerListResponse.CustomerSummary summary = new CustomerListResponse.CustomerSummary(
            "test-id-123", "John", "Doe", "john.doe@example.com", CustomerStatus.ACTIVE
        );
        when(customerRepository.findByCustomerStatus(eq(CustomerStatus.ACTIVE), any(Pageable.class))).thenReturn(customerPage);
        when(customerMapper.toSummary(customer)).thenReturn(summary);

        // When
        CustomerListResponse result = customerService.getCustomers(0, 10, CustomerStatus.ACTIVE);

        // Then
        assertThat(result.customers()).hasSize(1);
        assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.page()).isZero();
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    void should_GetCustomers_WithDefaultPageAndSize_When_InvalidPageAndSize() {
        // Given
        List<Customer> customers = List.of();
        Page<Customer> customerPage = new PageImpl<>(customers, PageRequest.of(0, 10), 0);
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(customerPage);

        // When
        CustomerListResponse result = customerService.getCustomers(-1, 0, null);

        // Then
        assertThat(result.customers()).isEmpty();
        assertThat(result.page()).isZero();
        assertThat(result.size()).isEqualTo(10);
    }
    
    @Test
    void should_SearchCustomers_ReturnResults() {
        // Given
        List<Customer> customers = List.of(customer);
        Page<Customer> customerPage = new PageImpl<>(customers, PageRequest.of(0, 10), 1);
        CustomerListResponse.CustomerSummary summary = new CustomerListResponse.CustomerSummary(
            "test-id-123", "John", "Doe", "john.doe@example.com", CustomerStatus.ACTIVE
        );
        when(customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(eq("John"), eq("John"), any(Pageable.class))).thenReturn(customerPage);
        when(customerMapper.toSummary(customer)).thenReturn(summary);

        // When
        CustomerListResponse result = customerService.searchCustomers("John", 0, 10);

        // Then
        assertThat(result.customers()).hasSize(1);
        assertThat(result.page()).isZero();
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    void should_SearchCustomers_ReturnEmpty_When_NoResults() {
        // Given
        Page<Customer> customerPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(eq("Jane"), eq("Jane"), any(Pageable.class))).thenReturn(customerPage);

        // When
        CustomerListResponse result = customerService.searchCustomers("Jane", 0, 10);

        // Then
        assertThat(result.customers()).isEmpty();
        assertThat(result.page()).isZero();
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    void should_SearchCustomers_WithDefaultPageAndSize_When_InvalidPageAndSize() {
        // Given
        Page<Customer> customerPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(anyString(), anyString(), any(Pageable.class))).thenReturn(customerPage);

        // When
        CustomerListResponse result = customerService.searchCustomers("John", -1, 0);

        // Then
        assertThat(result.customers()).isEmpty();
        assertThat(result.page()).isZero();
        assertThat(result.size()).isEqualTo(10);
    }
    
    @Test
    void should_UpdateCustomer_When_CustomerExists() {
        // Given
        when(customerRepository.findById("test-id-123")).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmailAndCustomerIdNot(updateRequest.email(), "test-id-123"))
            .thenReturn(false);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toResponse(customer)).thenReturn(customerResponse);
        
        // When
        CustomerResponse result = customerService.updateCustomer("test-id-123", updateRequest);
        
        // Then
        assertThat(result).isEqualTo(customerResponse);
        verify(customerMapper).updateEntityFromRequest(updateRequest, customer);
        verify(customerRepository).save(customer);
    }
    
    @Test
    void should_ThrowDuplicateEmailException_When_UpdateEmailAlreadyExists() {
        // Given
        when(customerRepository.findById("test-id-123")).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmailAndCustomerIdNot(updateRequest.email(), "test-id-123"))
            .thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> customerService.updateCustomer("test-id-123", updateRequest))
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessageContaining(updateRequest.email());
        
        verify(customerRepository, never()).save(any(Customer.class));
    }
    
    @Test
    void should_UpdateCustomer_When_EmailNotChanged() {
        // Given
        customer.setEmail(updateRequest.email());
        when(customerRepository.findById("test-id-123")).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toResponse(customer)).thenReturn(customerResponse);

        // When
        CustomerResponse result = customerService.updateCustomer("test-id-123", updateRequest);

        // Then
        assertThat(result).isEqualTo(customerResponse);
        verify(customerMapper).updateEntityFromRequest(updateRequest, customer);
        verify(customerRepository).save(customer);
    }
    
    @Test
    void should_DeleteCustomer_When_CustomerExists() {
        // Given
        when(customerRepository.existsById("test-id-123")).thenReturn(true);
        
        // When
        customerService.deleteCustomer("test-id-123");
        
        // Then
        verify(customerRepository).deleteById("test-id-123");
    }
    
    @Test
    void should_ThrowCustomerNotFoundException_When_DeletingNonexistentCustomer() {
        // Given
        when(customerRepository.existsById("nonexistent-id")).thenReturn(false);
        
        // When & Then
        assertThatThrownBy(() -> customerService.deleteCustomer("nonexistent-id"))
            .isInstanceOf(CustomerNotFoundException.class)
            .hasMessageContaining("nonexistent-id");
        
        verify(customerRepository, never()).deleteById(anyString());
    }
    
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentCustomer() {
        // Given
        String customerId = "nonexistent-id";
        UpdateCustomerRequest request = new UpdateCustomerRequest(
            "Jane", "Smith", "jane.smith@example.com", "+1234567890", 
            "123 Main St", null, null
        );
        
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> customerService.updateCustomer(customerId, request))
            .isInstanceOf(CustomerNotFoundException.class)
            .hasMessage("Customer not found with ID: " + customerId);
        
        verify(customerRepository).findById(customerId);
        verifyNoMoreInteractions(customerRepository);
        verifyNoInteractions(customerMapper);
    }
}
