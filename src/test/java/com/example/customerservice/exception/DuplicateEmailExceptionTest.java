package com.example.customerservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for DuplicateEmailException.
 */
class DuplicateEmailExceptionTest {

    @Test
    @DisplayName("Should create exception with custom message")
    void shouldCreateExceptionWithCustomMessage() {
        // Given
        String email = "test@example.com";

        // When
        DuplicateEmailException exception = new DuplicateEmailException(email);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Customer with email 'test@example.com' already exists");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should create exception with null message")
    void shouldCreateExceptionWithNullMessage() {
        // When
        DuplicateEmailException exception = new DuplicateEmailException(null);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Customer with email 'null' already exists");
    }

    @Test
    @DisplayName("Should create exception with empty message")
    void shouldCreateExceptionWithEmptyMessage() {
        // When
        DuplicateEmailException exception = new DuplicateEmailException("");

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Customer with email '' already exists");
    }
}
