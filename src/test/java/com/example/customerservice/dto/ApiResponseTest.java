package com.example.customerservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ApiResponse DTO.
 */
class ApiResponseTest {

    @Test
    @DisplayName("Should create successful response with message and data")
    void shouldCreateSuccessfulResponseWithMessageAndData() {
        // Given
        String message = "Operation successful";
        String data = "test data";

        // When
        ApiResponse<String> response = ApiResponse.success(message, data);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("SUCCESS");
        assertThat(response.message()).isEqualTo(message);
        assertThat(response.data()).isEqualTo(data);
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.timestamp()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    @DisplayName("Should create successful response with data and default message")
    void shouldCreateSuccessfulResponseWithDataAndDefaultMessage() {
        // Given
        Integer data = 12345;

        // When
        ApiResponse<Integer> response = ApiResponse.success(data);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("SUCCESS");
        assertThat(response.message()).isEqualTo("Operation completed successfully");
        assertThat(response.data()).isEqualTo(data);
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.timestamp()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    @DisplayName("Should create error response with message")
    void shouldCreateErrorResponseWithMessage() {
        // Given
        String errorMessage = "Something went wrong";

        // When
        ApiResponse<Object> response = ApiResponse.error(errorMessage);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("ERROR");
        assertThat(response.message()).isEqualTo(errorMessage);
        assertThat(response.data()).isNull();
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.timestamp()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    @DisplayName("Should create error response with message and error data")
    void shouldCreateErrorResponseWithMessageAndErrorData() {
        // Given
        String errorMessage = "Validation failed";
        java.util.Map<String, String> errorData = java.util.Map.of(
                "firstName", "First name is required",
                "email", "Email must be valid"
        );

        // When
        ApiResponse<java.util.Map<String, String>> response = ApiResponse.error(errorMessage, errorData);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("ERROR");
        assertThat(response.message()).isEqualTo(errorMessage);
        assertThat(response.data()).isEqualTo(errorData);
        assertThat(response.data()).containsKeys("firstName", "email");
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.timestamp()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    @DisplayName("Should handle null message in success response")
    void shouldHandleNullMessageInSuccessResponse() {
        // Given
        String data = "test data";

        // When
        ApiResponse<String> response = ApiResponse.success(null, data);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("SUCCESS");
        assertThat(response.message()).isNull();
        assertThat(response.data()).isEqualTo(data);
        assertThat(response.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle null data in success response")
    void shouldHandleNullDataInSuccessResponse() {
        // When
        ApiResponse<Object> response = ApiResponse.success(null);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("SUCCESS");
        assertThat(response.message()).isEqualTo("Operation completed successfully");
        assertThat(response.data()).isNull();
        assertThat(response.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle null message in error response")
    void shouldHandleNullMessageInErrorResponse() {
        // When
        ApiResponse<Object> response = ApiResponse.error(null);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("ERROR");
        assertThat(response.message()).isNull();
        assertThat(response.data()).isNull();
        assertThat(response.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle null data in error response with message and data")
    void shouldHandleNullDataInErrorResponseWithMessageAndData() {
        // Given
        String errorMessage = "Error occurred";

        // When
        ApiResponse<Object> response = ApiResponse.error(errorMessage, null);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("ERROR");
        assertThat(response.message()).isEqualTo(errorMessage);
        assertThat(response.data()).isNull();
        assertThat(response.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should create response with record constructor")
    void shouldCreateResponseWithRecordConstructor() {
        // Given
        String status = "CUSTOM";
        String message = "Custom message";
        String data = "custom data";
        Instant timestamp = Instant.now();

        // When
        ApiResponse<String> response = new ApiResponse<>(status, message, data, timestamp);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(status);
        assertThat(response.message()).isEqualTo(message);
        assertThat(response.data()).isEqualTo(data);
        assertThat(response.timestamp()).isEqualTo(timestamp);
    }
}
