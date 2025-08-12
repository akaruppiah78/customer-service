package com.example.customerservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for CustomerNotFoundException.
 */
class CustomerNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with custom message")
    void shouldCreateExceptionWithCustomMessage() {
        // Given
        String customerId = "123";

        // When
        CustomerNotFoundException exception = new CustomerNotFoundException(customerId);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Customer not found with ID: 123");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should create exception with null message")
    void shouldCreateExceptionWithNullMessage() {
        // When
        CustomerNotFoundException exception = new CustomerNotFoundException(null);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Customer not found with ID: null");
    }

    @Test
    @DisplayName("Should create exception with empty message")
    void shouldCreateExceptionWithEmptyMessage() {
        // When
        CustomerNotFoundException exception = new CustomerNotFoundException("");

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Customer not found with ID: ");
    }
}
