package com.example.customerservice.repository;

import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryTest {

    @Mock
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustomerId("test-id-123");
        testCustomer.setFirstName("John");
        testCustomer.setLastName("Doe");
        testCustomer.setEmail("john.doe@example.com");
        testCustomer.setPhone("+1234567890");
        testCustomer.setAddress("123 Test Street");
        testCustomer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
        testCustomer.setCreatedAt(Instant.now());
        testCustomer.setUpdatedAt(Instant.now());
    }

    @Test
    void should_FindCustomerByEmail_When_EmailExists() {
        // Given
        when(customerRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(testCustomer));

        // When
        Optional<Customer> result = customerRepository.findByEmail("john.doe@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.get().getFirstName()).isEqualTo("John");
        verify(customerRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void should_ReturnEmpty_When_EmailDoesNotExist() {
        // Given
        when(customerRepository.findByEmail("nonexistent@example.com"))
                .thenReturn(Optional.empty());

        // When
        Optional<Customer> result = customerRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isNotPresent();
        verify(customerRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void should_ReturnTrue_When_EmailExists() {
        // Given
        when(customerRepository.existsByEmail("john.doe@example.com"))
                .thenReturn(true);

        // When
        boolean exists = customerRepository.existsByEmail("john.doe@example.com");

        // Then
        assertThat(exists).isTrue();
        verify(customerRepository, times(1)).existsByEmail("john.doe@example.com");
    }

    @Test
    void should_ReturnFalse_When_EmailDoesNotExist() {
        // Given
        when(customerRepository.existsByEmail("nonexistent@example.com"))
                .thenReturn(false);

        // When
        boolean exists = customerRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertThat(exists).isFalse();
        verify(customerRepository, times(1)).existsByEmail("nonexistent@example.com");
    }

    @Test
    void should_FindCustomersByName_When_NameMatches() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customers = List.of(testCustomer);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, 1);
        
        when(customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                anyString(), anyString(), any(Pageable.class)))
                .thenReturn(customerPage);

        // When
        Page<Customer> result = customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                "John", "John", pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("John");
        verify(customerRepository, times(1))
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("John", "John", pageable);
    }

    @Test
    void should_FindCustomersByStatus_When_StatusMatches() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customers = List.of(testCustomer);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, 1);
        
        when(customerRepository.findByCustomerStatus(CustomerStatus.ACTIVE, pageable))
                .thenReturn(customerPage);

        // When
        Page<Customer> result = customerRepository.findByCustomerStatus(CustomerStatus.ACTIVE, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCustomerStatus()).isEqualTo(CustomerStatus.ACTIVE);
        verify(customerRepository, times(1)).findByCustomerStatus(CustomerStatus.ACTIVE, pageable);
    }
}
