package com.example.customerservice.mapper;

import com.example.customerservice.dto.CreateCustomerRequest;
import com.example.customerservice.dto.CustomerResponse;
import com.example.customerservice.dto.CustomerListResponse.CustomerSummary;
import com.example.customerservice.dto.UpdateCustomerRequest;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for CustomerMapper.
 * Tests all mapping methods and handles null value scenarios.
 */
class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Test
    @DisplayName("Should map CreateCustomerRequest to Customer entity with default ACTIVE status")
    void shouldMapCreateRequestToEntity() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest(
                "John",
                "Doe", 
                "john.doe@example.com",
                "+1234567890",
                "123 Main St",
                LocalDate.of(1990, 1, 15),
                null  // null status should default to ACTIVE
        );

        // When
        Customer customer = customerMapper.toEntity(request);

        // Then
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstName()).isEqualTo("John");
        assertThat(customer.getLastName()).isEqualTo("Doe");
        assertThat(customer.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(customer.getPhone()).isEqualTo("+1234567890");
        assertThat(customer.getAddress()).isEqualTo("123 Main St");
        assertThat(customer.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 15));
        assertThat(customer.getCustomerStatus()).isEqualTo(CustomerStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should map CreateCustomerRequest to Customer entity with provided status")
    void shouldMapCreateRequestToEntityWithProvidedStatus() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest(
                "Jane",
                "Smith",
                "jane.smith@example.com", 
                "+9876543210",
                null,  // null address
                null,  // null date of birth
                CustomerStatus.INACTIVE
        );

        // When
        Customer customer = customerMapper.toEntity(request);

        // Then
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstName()).isEqualTo("Jane");
        assertThat(customer.getLastName()).isEqualTo("Smith");
        assertThat(customer.getEmail()).isEqualTo("jane.smith@example.com");
        assertThat(customer.getPhone()).isEqualTo("+9876543210");
        assertThat(customer.getAddress()).isNull();
        assertThat(customer.getDateOfBirth()).isNull();
        assertThat(customer.getCustomerStatus()).isEqualTo(CustomerStatus.INACTIVE);
    }

    @Test
    @DisplayName("Should map Customer entity to CustomerResponse")
    void shouldMapEntityToResponse() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId("507f1f77bcf86cd799439011");
        customer.setFirstName("Alice");
        customer.setLastName("Johnson");
        customer.setEmail("alice.johnson@example.com");
        customer.setPhone("+1555123456");
        customer.setAddress("456 Oak Ave");
        customer.setDateOfBirth(LocalDate.of(1985, 3, 20));
        customer.setCustomerStatus(CustomerStatus.ACTIVE);
        customer.setCreatedAt(Instant.parse("2023-01-01T10:00:00Z"));
        customer.setUpdatedAt(Instant.parse("2023-01-02T15:30:00Z"));

        // When
        CustomerResponse response = customerMapper.toResponse(customer);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.customerId()).isEqualTo("507f1f77bcf86cd799439011");
        assertThat(response.firstName()).isEqualTo("Alice");
        assertThat(response.lastName()).isEqualTo("Johnson");
        assertThat(response.email()).isEqualTo("alice.johnson@example.com");
        assertThat(response.phone()).isEqualTo("+1555123456");
        assertThat(response.address()).isEqualTo("456 Oak Ave");
        assertThat(response.dateOfBirth()).isEqualTo(LocalDate.of(1985, 3, 20));
        assertThat(response.customerStatus()).isEqualTo(CustomerStatus.ACTIVE);
        assertThat(response.createdAt()).isEqualTo(Instant.parse("2023-01-01T10:00:00Z"));
        assertThat(response.updatedAt()).isEqualTo(Instant.parse("2023-01-02T15:30:00Z"));
    }

    @Test
    @DisplayName("Should map Customer entity to CustomerSummary")
    void shouldMapEntityToSummary() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId("507f1f77bcf86cd799439012");
        customer.setFirstName("Bob");
        customer.setLastName("Wilson");
        customer.setEmail("bob.wilson@example.com");
        customer.setPhone("+1777888999");
        customer.setCustomerStatus(CustomerStatus.SUSPENDED);

        // When
        CustomerSummary summary = customerMapper.toSummary(customer);

        // Then
        assertThat(summary).isNotNull();
        assertThat(summary.customerId()).isEqualTo("507f1f77bcf86cd799439012");
        assertThat(summary.firstName()).isEqualTo("Bob");
        assertThat(summary.lastName()).isEqualTo("Wilson");
        assertThat(summary.email()).isEqualTo("bob.wilson@example.com");
        assertThat(summary.customerStatus()).isEqualTo(CustomerStatus.SUSPENDED);
    }

    @Test
    @DisplayName("Should update Customer entity from UpdateCustomerRequest with all fields provided")
    void shouldUpdateEntityFromRequestWithAllFields() {
        // Given
        Customer customer = new Customer();
        customer.setFirstName("OldFirst");
        customer.setLastName("OldLast");
        customer.setEmail("old@example.com");
        customer.setPhone("+1111111111");
        customer.setAddress("Old Address");
        customer.setDateOfBirth(LocalDate.of(1980, 1, 1));
        customer.setCustomerStatus(CustomerStatus.INACTIVE);

        UpdateCustomerRequest request = new UpdateCustomerRequest(
                "NewFirst",
                "NewLast",
                "new@example.com",
                "+2222222222",
                "New Address",
                LocalDate.of(1990, 12, 25),
                CustomerStatus.ACTIVE
        );

        // When
        customerMapper.updateEntityFromRequest(request, customer);

        // Then
        assertThat(customer.getFirstName()).isEqualTo("NewFirst");
        assertThat(customer.getLastName()).isEqualTo("NewLast");
        assertThat(customer.getEmail()).isEqualTo("new@example.com");
        assertThat(customer.getPhone()).isEqualTo("+2222222222");
        assertThat(customer.getAddress()).isEqualTo("New Address");
        assertThat(customer.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 12, 25));
        assertThat(customer.getCustomerStatus()).isEqualTo(CustomerStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should update Customer entity from UpdateCustomerRequest with partial fields")
    void shouldUpdateEntityFromRequestWithPartialFields() {
        // Given
        Customer customer = new Customer();
        customer.setFirstName("Original");
        customer.setLastName("Customer");
        customer.setEmail("original@example.com");
        customer.setPhone("+1111111111");
        customer.setAddress("Original Address");
        customer.setDateOfBirth(LocalDate.of(1980, 1, 1));
        customer.setCustomerStatus(CustomerStatus.INACTIVE);

        UpdateCustomerRequest request = new UpdateCustomerRequest(
                "Updated",  // only firstName updated
                null,       // lastName not updated
                null,       // email not updated
                "+3333333333", // phone updated
                null,       // address not updated
                null,       // dateOfBirth not updated
                CustomerStatus.ACTIVE // status updated
        );

        // When
        customerMapper.updateEntityFromRequest(request, customer);

        // Then
        assertThat(customer.getFirstName()).isEqualTo("Updated");
        assertThat(customer.getLastName()).isEqualTo("Customer");  // unchanged
        assertThat(customer.getEmail()).isEqualTo("original@example.com");  // unchanged
        assertThat(customer.getPhone()).isEqualTo("+3333333333");
        assertThat(customer.getAddress()).isEqualTo("Original Address");  // unchanged
        assertThat(customer.getDateOfBirth()).isEqualTo(LocalDate.of(1980, 1, 1));  // unchanged
        assertThat(customer.getCustomerStatus()).isEqualTo(CustomerStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should not update Customer entity when all UpdateCustomerRequest fields are null")
    void shouldNotUpdateEntityWhenAllFieldsAreNull() {
        // Given
        Customer customer = new Customer();
        customer.setFirstName("Unchanged");
        customer.setLastName("Customer");
        customer.setEmail("unchanged@example.com");
        customer.setPhone("+4444444444");
        customer.setAddress("Unchanged Address");
        customer.setDateOfBirth(LocalDate.of(1975, 6, 15));
        customer.setCustomerStatus(CustomerStatus.SUSPENDED);

        UpdateCustomerRequest request = new UpdateCustomerRequest(
                null, null, null, null, null, null, null
        );

        // When
        customerMapper.updateEntityFromRequest(request, customer);

        // Then - all fields should remain unchanged
        assertThat(customer.getFirstName()).isEqualTo("Unchanged");
        assertThat(customer.getLastName()).isEqualTo("Customer");
        assertThat(customer.getEmail()).isEqualTo("unchanged@example.com");
        assertThat(customer.getPhone()).isEqualTo("+4444444444");
        assertThat(customer.getAddress()).isEqualTo("Unchanged Address");
        assertThat(customer.getDateOfBirth()).isEqualTo(LocalDate.of(1975, 6, 15));
        assertThat(customer.getCustomerStatus()).isEqualTo(CustomerStatus.SUSPENDED);
    }
}
