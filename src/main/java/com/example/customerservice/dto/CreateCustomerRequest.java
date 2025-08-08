package com.example.customerservice.dto;

import com.example.customerservice.model.CustomerStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Data Transfer Object for customer creation requests.
 * 
 * <p>This record encapsulates all the information required to create a new customer
 * in the system. It includes comprehensive validation annotations to ensure data
 * integrity and business rule compliance.</p>
 * 
 * <p>Validation rules:</p>
 * <ul>
 *   <li>First and last names are required and limited to 50 characters</li>
 *   <li>Email is required, must be valid format, and will be checked for uniqueness</li>
 *   <li>Phone number is required and must be in international format (10-15 digits)</li>
 *   <li>Address is optional but limited to 200 characters</li>
 *   <li>Date of birth is optional</li>
 *   <li>Customer status is optional and defaults to ACTIVE</li>
 * </ul>
 * 
 * @param firstName customer's first name (required, max 50 characters)
 * @param lastName customer's last name (required, max 50 characters)
 * @param email customer's email address (required, must be valid and unique)
 * @param phone customer's phone number (required, international format)
 * @param address customer's address (optional, max 200 characters)
 * @param dateOfBirth customer's date of birth (optional)
 * @param customerStatus customer's initial status (optional, defaults to ACTIVE)
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 */
public record CreateCustomerRequest(
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    String lastName,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be in international format")
    String phone,
    
    @Size(max = 200, message = "Address must not exceed 200 characters")
    String address,
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth,
    
    CustomerStatus customerStatus
) {}
