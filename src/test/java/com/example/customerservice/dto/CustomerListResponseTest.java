package com.example.customerservice.dto;

import com.example.customerservice.model.CustomerStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for CustomerListResponse DTO and nested CustomerSummary.
 */
class CustomerListResponseTest {

    @Test
    @DisplayName("Should create CustomerListResponse with customer data")
    void shouldCreateCustomerListResponseWithCustomerData() {
        // Given
        CustomerListResponse.CustomerSummary customer1 = new CustomerListResponse.CustomerSummary(
                "1", "John", "Doe", "john@example.com", CustomerStatus.ACTIVE
        );
        CustomerListResponse.CustomerSummary customer2 = new CustomerListResponse.CustomerSummary(
                "2", "Jane", "Smith", "jane@example.com", CustomerStatus.INACTIVE
        );
        List<CustomerListResponse.CustomerSummary> customers = Arrays.asList(customer1, customer2);

        // When
        CustomerListResponse response = new CustomerListResponse(
                customers, 0, 10, 25, 3, true, false
        );

        // Then
        assertThat(response).isNotNull();
        assertThat(response.customers()).hasSize(2);
        assertThat(response.customers()).containsExactly(customer1, customer2);
        assertThat(response.page()).isZero();
        assertThat(response.size()).isEqualTo(10);
        assertThat(response.totalElements()).isEqualTo(25);
        assertThat(response.totalPages()).isEqualTo(3);
        assertThat(response.hasNext()).isTrue();
        assertThat(response.hasPrevious()).isFalse();
    }

    @Test
    @DisplayName("Should create CustomerListResponse with empty customer list")
    void shouldCreateCustomerListResponseWithEmptyCustomerList() {
        // Given
        List<CustomerListResponse.CustomerSummary> emptyCustomers = Collections.emptyList();

        // When
        CustomerListResponse response = new CustomerListResponse(
                emptyCustomers, 0, 10, 0, 0, false, false
        );

        // Then
        assertThat(response).isNotNull();
        assertThat(response.customers()).isEmpty();
        assertThat(response.page()).isZero();
        assertThat(response.size()).isEqualTo(10);
        assertThat(response.totalElements()).isZero();
        assertThat(response.totalPages()).isZero();
        assertThat(response.hasNext()).isFalse();
        assertThat(response.hasPrevious()).isFalse();
    }

    @Test
    @DisplayName("Should create CustomerListResponse for middle page")
    void shouldCreateCustomerListResponseForMiddlePage() {
        // Given
        CustomerListResponse.CustomerSummary customer = new CustomerListResponse.CustomerSummary(
                "1", "John", "Doe", "john@example.com", CustomerStatus.ACTIVE
        );
        List<CustomerListResponse.CustomerSummary> customers = List.of(customer);

        // When - middle page (page 1 of 3)
        CustomerListResponse response = new CustomerListResponse(
                customers, 1, 10, 25, 3, true, true
        );

        // Then
        assertThat(response).isNotNull();
        assertThat(response.customers()).hasSize(1);
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.size()).isEqualTo(10);
        assertThat(response.totalElements()).isEqualTo(25);
        assertThat(response.totalPages()).isEqualTo(3);
        assertThat(response.hasNext()).isTrue();   // has next page
        assertThat(response.hasPrevious()).isTrue(); // has previous page
    }

    @Test
    @DisplayName("Should create CustomerListResponse for last page")
    void shouldCreateCustomerListResponseForLastPage() {
        // Given
        CustomerListResponse.CustomerSummary customer = new CustomerListResponse.CustomerSummary(
                "1", "John", "Doe", "john@example.com", CustomerStatus.SUSPENDED
        );
        List<CustomerListResponse.CustomerSummary> customers = List.of(customer);

        // When - last page (page 2 of 3)
        CustomerListResponse response = new CustomerListResponse(
                customers, 2, 10, 25, 3, false, true
        );

        // Then
        assertThat(response).isNotNull();
        assertThat(response.customers()).hasSize(1);
        assertThat(response.page()).isEqualTo(2);
        assertThat(response.hasNext()).isFalse();  // no next page
        assertThat(response.hasPrevious()).isTrue(); // has previous page
    }

    @Test
    @DisplayName("Should create CustomerSummary with all fields")
    void shouldCreateCustomerSummaryWithAllFields() {
        // Given
        String customerId = "507f1f77bcf86cd799439011";
        String firstName = "Alice";
        String lastName = "Johnson";
        String email = "alice.johnson@example.com";
        CustomerStatus status = CustomerStatus.ACTIVE;

        // When
        CustomerListResponse.CustomerSummary summary = new CustomerListResponse.CustomerSummary(
                customerId, firstName, lastName, email, status
        );

        // Then
        assertThat(summary).isNotNull();
        assertThat(summary.customerId()).isEqualTo(customerId);
        assertThat(summary.firstName()).isEqualTo(firstName);
        assertThat(summary.lastName()).isEqualTo(lastName);
        assertThat(summary.email()).isEqualTo(email);
        assertThat(summary.customerStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should demonstrate CustomerSummary record equality")
    void shouldDemonstrateCustomerSummaryRecordEquality() {
        // Given
        CustomerListResponse.CustomerSummary summary1 = new CustomerListResponse.CustomerSummary(
                "123", "John", "Doe", "john@example.com", CustomerStatus.ACTIVE
        );
        CustomerListResponse.CustomerSummary summary2 = new CustomerListResponse.CustomerSummary(
                "123", "John", "Doe", "john@example.com", CustomerStatus.ACTIVE
        );

        // Then
        assertThat(summary1).isEqualTo(summary2)
                .hasSameHashCodeAs(summary2);
    }

    @Test
    @DisplayName("Should demonstrate CustomerSummary record toString")
    void shouldDemonstrateCustomerSummaryRecordToString() {
        // Given
        CustomerListResponse.CustomerSummary summary = new CustomerListResponse.CustomerSummary(
                "123", "John", "Doe", "john@example.com", CustomerStatus.ACTIVE
        );

        // When
        String toString = summary.toString();

        // Then
        assertThat(toString).contains("CustomerSummary")
                .contains("customerId=123")
                .contains("firstName=John")
                .contains("lastName=Doe");
    }

    @Test
    @DisplayName("Should test all CustomerStatus values in CustomerSummary")
    void shouldTestAllCustomerStatusValuesInCustomerSummary() {
        // Test all enum values work correctly
        for (CustomerStatus status : CustomerStatus.values()) {
            CustomerListResponse.CustomerSummary summary = new CustomerListResponse.CustomerSummary(
                    "test-id", "Test", "User", "test@example.com", status
            );

            assertThat(summary.customerStatus()).isEqualTo(status);
        }
    }

    @Test
    @DisplayName("Should demonstrate record equality for CustomerListResponse")
    void shouldDemonstrateRecordEqualityForCustomerListResponse() {
        // Given
        List<CustomerListResponse.CustomerSummary> customers = List.of(
                new CustomerListResponse.CustomerSummary("1", "John", "Doe", "john@example.com", CustomerStatus.ACTIVE)
        );

        CustomerListResponse response1 = new CustomerListResponse(
                customers, 0, 10, 1, 1, false, false
        );
        CustomerListResponse response2 = new CustomerListResponse(
                customers, 0, 10, 1, 1, false, false
        );

        // Then
        assertThat(response1).isEqualTo(response2)
                .hasSameHashCodeAs(response2);
    }
}
