package com.example.customerservice.dto;

import com.example.customerservice.model.CustomerStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Data Transfer Object for customer update requests.
 * 
 * <p>This record encapsulates all the information that can be modified when updating
 * an existing customer. All fields are optional, allowing for partial updates where
 * only specific customer attributes need to be modified.</p>
 * 
 * <p>Validation rules for provided fields:</p>
 * <ul>
 *   <li>First and last names are limited to 50 characters when provided</li>
 *   <li>Email must be valid format when provided and will be checked for uniqueness</li>
 *   <li>Phone number must be in international format when provided (10-15 digits)</li>
 *   <li>Address is limited to 200 characters when provided</li>
 *   <li>Date of birth accepts valid date format</li>
 *   <li>Customer status can be changed to any valid status</li>
 * </ul>
 * 
 * <p>Note: Null values indicate that the corresponding field should not be updated,
 * effectively implementing a partial update pattern.</p>
 * 
 * @param firstName customer's updated first name (optional, max 50 characters)
 * @param lastName customer's updated last name (optional, max 50 characters)
 * @param email customer's updated email address (optional, must be valid and unique)
 * @param phone customer's updated phone number (optional, international format)
 * @param address customer's updated address (optional, max 200 characters)
 * @param dateOfBirth customer's updated date of birth (optional)
 * @param customerStatus customer's updated status (optional)
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 */
public record UpdateCustomerRequest(
    @Size(max = 50, message = "First name must not exceed 50 characters")
    String firstName,
    
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    String lastName,
    
    @Email(message = "Email must be valid")
    String email,
    
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be in international format")
    String phone,
    
    @Size(max = 200, message = "Address must not exceed 200 characters")
    String address,
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth,
    
    CustomerStatus customerStatus
) {}
