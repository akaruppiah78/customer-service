package com.example.customerservice.dto;

import com.example.customerservice.model.CustomerStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for CustomerResponse DTO.
 */
class CustomerResponseTest {

    @Test
    @DisplayName("Should create CustomerResponse with all fields")
    void shouldCreateCustomerResponseWithAllFields() {
        // Given
        String customerId = "507f1f77bcf86cd799439011";
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String phone = "+1234567890";
        String address = "123 Main St";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 15);
        CustomerStatus status = CustomerStatus.ACTIVE;
        Instant createdAt = Instant.parse("2023-01-01T10:00:00Z");
        Instant updatedAt = Instant.parse("2023-01-02T15:30:00Z");

        // When
        CustomerResponse response = new CustomerResponse(
                customerId, firstName, lastName, email, phone,
                address, dateOfBirth, status, createdAt, updatedAt
        );

        // Then
        assertThat(response).isNotNull();
        assertThat(response.customerId()).isEqualTo(customerId);
        assertThat(response.firstName()).isEqualTo(firstName);
        assertThat(response.lastName()).isEqualTo(lastName);
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.phone()).isEqualTo(phone);
        assertThat(response.address()).isEqualTo(address);
        assertThat(response.dateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(response.customerStatus()).isEqualTo(status);
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.updatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should create CustomerResponse with null optional fields")
    void shouldCreateCustomerResponseWithNullOptionalFields() {
        // Given
        String customerId = "507f1f77bcf86cd799439012";
        String firstName = "Jane";
        String lastName = "Smith";
        String email = "jane.smith@example.com";
        String phone = "+9876543210";
        CustomerStatus status = CustomerStatus.INACTIVE;
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        // When
        CustomerResponse response = new CustomerResponse(
                customerId, firstName, lastName, email, phone,
                null,  // address can be null
                null,  // dateOfBirth can be null
                status, createdAt, updatedAt
        );

        // Then
        assertThat(response).isNotNull();
        assertThat(response.customerId()).isEqualTo(customerId);
        assertThat(response.firstName()).isEqualTo(firstName);
        assertThat(response.lastName()).isEqualTo(lastName);
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.phone()).isEqualTo(phone);
        assertThat(response.address()).isNull();
        assertThat(response.dateOfBirth()).isNull();
        assertThat(response.customerStatus()).isEqualTo(status);
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.updatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should demonstrate record equality")
    void shouldDemonstrateRecordEquality() {
        // Given
        Instant timestamp = Instant.parse("2023-01-01T10:00:00Z");
        
        CustomerResponse response1 = new CustomerResponse(
                "123", "John", "Doe", "john@example.com", "+1234567890",
                "123 St", LocalDate.of(1990, 1, 1), CustomerStatus.ACTIVE,
                timestamp, timestamp
        );
        
        CustomerResponse response2 = new CustomerResponse(
                "123", "John", "Doe", "john@example.com", "+1234567890",
                "123 St", LocalDate.of(1990, 1, 1), CustomerStatus.ACTIVE,
                timestamp, timestamp
        );

        // Then
        assertThat(response1).isEqualTo(response2)
                .hasSameHashCodeAs(response2);
    }

    @Test
    @DisplayName("Should demonstrate record toString")
    void shouldDemonstrateRecordToString() {
        // Given
        CustomerResponse response = new CustomerResponse(
                "123", "John", "Doe", "john@example.com", "+1234567890",
                null, null, CustomerStatus.ACTIVE,
                Instant.parse("2023-01-01T10:00:00Z"),
                Instant.parse("2023-01-01T10:00:00Z")
        );

        // When
        String toString = response.toString();

        // Then
        assertThat(toString).contains("CustomerResponse")
                .contains("customerId=123")
                .contains("firstName=John")
                .contains("lastName=Doe")
                .contains("email=john@example.com");
    }
}
