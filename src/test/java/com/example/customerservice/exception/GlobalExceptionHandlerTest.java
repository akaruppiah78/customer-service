package com.example.customerservice.exception;

import com.example.customerservice.dto.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GlobalExceptionHandler.
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should handle CustomerNotFoundException and return NOT_FOUND status")
    void shouldHandleCustomerNotFoundException() {
        // Given
        CustomerNotFoundException exception = new CustomerNotFoundException("123");

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleCustomerNotFound(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo("ERROR");
        assertThat(response.getBody().message()).isEqualTo("Customer not found with ID: 123");
        assertThat(response.getBody().data()).isNull();
    }

    @Test
    @DisplayName("Should handle DuplicateEmailException and return CONFLICT status")
    void shouldHandleDuplicateEmailException() {
        // Given
        DuplicateEmailException exception = new DuplicateEmailException("test@example.com");

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleDuplicateEmail(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo("ERROR");
        assertThat(response.getBody().message()).isEqualTo("Customer with email 'test@example.com' already exists");
        assertThat(response.getBody().data()).isNull();
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException and return BAD_REQUEST status")
    void shouldHandleMethodArgumentNotValidException() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("customer", "firstName", "First name is required");
        FieldError fieldError2 = new FieldError("customer", "email", "Email must be valid");
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.List.of(fieldError1, fieldError2));

        // When
        ResponseEntity<ApiResponse<Map<String, String>>> response = globalExceptionHandler.handleValidationErrors(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo("ERROR");
        assertThat(response.getBody().message()).contains("Validation failed");
        assertThat(response.getBody().data()).isNotNull();
        assertThat(response.getBody().data()).hasSize(2);
        assertThat(response.getBody().data()).containsEntry("firstName", "First name is required");
        assertThat(response.getBody().data()).containsEntry("email", "Email must be valid");
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException and return BAD_REQUEST status")
    void shouldHandleIllegalArgumentException() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument provided");

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleIllegalArgument(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo("ERROR");
        assertThat(response.getBody().message()).isEqualTo("Invalid argument provided");
        assertThat(response.getBody().data()).isNull();
    }

    @Test
    @DisplayName("Should handle general Exception and return INTERNAL_SERVER_ERROR status")
    void shouldHandleGeneralException() {
        // Given
        Exception exception = new RuntimeException("Something went wrong");

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleGeneralException(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo("ERROR");
        assertThat(response.getBody().message()).isEqualTo("An unexpected error occurred");
        assertThat(response.getBody().data()).isNull();
    }

    @Test
    @DisplayName("Should handle CustomerNotFoundException with null message")
    void shouldHandleCustomerNotFoundExceptionWithNullMessage() {
        // Given
        CustomerNotFoundException exception = new CustomerNotFoundException(null);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleCustomerNotFound(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo("ERROR");
        assertThat(response.getBody().message()).isEqualTo("Customer not found with ID: null");
    }

    @Test
    @DisplayName("Should handle DuplicateEmailException with null message")
    void shouldHandleDuplicateEmailExceptionWithNullMessage() {
        // Given
        DuplicateEmailException exception = new DuplicateEmailException(null);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleDuplicateEmail(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo("ERROR");
        assertThat(response.getBody().message()).isEqualTo("Customer with email 'null' already exists");
    }
}
