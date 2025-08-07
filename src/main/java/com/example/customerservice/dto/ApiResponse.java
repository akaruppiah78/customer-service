package com.example.customerservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;

public record ApiResponse<T>(
    String status,
    String message,
    T data,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    Instant timestamp
) {
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("SUCCESS", message, data, Instant.now());
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return success("Operation completed successfully", data);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", message, null, Instant.now());
    }
    
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>("ERROR", message, data, Instant.now());
    }
}
