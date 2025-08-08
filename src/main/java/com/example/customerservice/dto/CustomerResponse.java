package com.example.customerservice.dto;

import com.example.customerservice.model.CustomerStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.Instant;

/**
 * Data Transfer Object representing customer information in API responses.
 * 
 * <p>This record encapsulates all customer data that is returned to clients
 * through REST API endpoints. It includes complete customer information along
 * with audit fields to track creation and modification timestamps.</p>
 * 
 * <p>The response includes:</p>
 * <ul>
 *   <li>All customer personal information (name, email, phone, address)</li>
 *   <li>Customer business information (status, date of birth)</li>
 *   <li>System audit information (creation and last update timestamps)</li>
 * </ul>
 * 
 * <p>Date formatting is controlled through Jackson annotations to ensure
 * consistent ISO format in JSON responses.</p>
 * 
 * @param customerId unique identifier for the customer
 * @param firstName customer's first name
 * @param lastName customer's last name
 * @param email customer's email address
 * @param phone customer's phone number in international format
 * @param address customer's physical address
 * @param dateOfBirth customer's date of birth (formatted as yyyy-MM-dd)
 * @param customerStatus current status of the customer account
 * @param createdAt timestamp when the customer was created (ISO format with UTC)
 * @param updatedAt timestamp when the customer was last updated (ISO format with UTC)
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 */
public record CustomerResponse(
    String customerId,
    String firstName,
    String lastName,
    String email,
    String phone,
    String address,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth,
    CustomerStatus customerStatus,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    Instant createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    Instant updatedAt
) {}
