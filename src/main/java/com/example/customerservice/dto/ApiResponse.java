package com.example.customerservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;

/**
 * Generic API response wrapper for all REST API endpoints.
 * 
 * <p>This record provides a standardized response format for all API operations,
 * ensuring consistent structure across all endpoints. It includes status information,
 * descriptive messages, response data, and timing information.</p>
 * 
 * <p>Response structure:</p>
 * <ul>
 *   <li>Status: SUCCESS or ERROR to indicate operation outcome</li>
 *   <li>Message: Human-readable description of the operation result</li>
 *   <li>Data: The actual response payload (null for error responses)</li>
 *   <li>Timestamp: When the response was generated (ISO format with UTC)</li>
 * </ul>
 * 
 * <p>Provides factory methods for creating standard success and error responses.</p>
 * 
 * @param <T> the type of data being returned in the response
 * @param status operation status (SUCCESS or ERROR)
 * @param message descriptive message about the operation result
 * @param data the response payload (can be null for errors)
 * @param timestamp when the response was generated (ISO format with UTC)
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 */
public record ApiResponse<T>(
    String status,
    String message,
    T data,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    Instant timestamp
) {
    
    /**
     * Creates a successful response with custom message and data.
     * 
     * @param <T> the type of data being returned
     * @param message descriptive success message
     * @param data the response payload
     * @return ApiResponse with SUCCESS status
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("SUCCESS", message, data, Instant.now());
    }
    
    /**
     * Creates a successful response with default message and provided data.
     * 
     * @param <T> the type of data being returned
     * @param data the response payload
     * @return ApiResponse with SUCCESS status and default message
     */
    public static <T> ApiResponse<T> success(T data) {
        return success("Operation completed successfully", data);
    }
    
    /**
     * Creates an error response with custom message and no data.
     * 
     * @param <T> the type of data (null for error responses)
     * @param message descriptive error message
     * @return ApiResponse with ERROR status and null data
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", message, null, Instant.now());
    }
    
    /**
     * Creates an error response with custom message and error data.
     * 
     * @param <T> the type of error data being returned
     * @param message descriptive error message
     * @param data error details or validation information
     * @return ApiResponse with ERROR status and error data
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>("ERROR", message, data, Instant.now());
    }
}
