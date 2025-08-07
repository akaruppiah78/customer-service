package com.example.customerservice.dto;

import java.util.List;

public record CustomerListResponse(
    List<CustomerSummary> customers,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrevious
) {
    
    public record CustomerSummary(
        String customerId,
        String firstName,
        String lastName,
        String email,
        com.example.customerservice.model.CustomerStatus customerStatus
    ) {}
}
