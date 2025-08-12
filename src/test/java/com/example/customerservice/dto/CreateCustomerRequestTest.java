package com.example.customerservice.dto;

import com.example.customerservice.model.CustomerStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for CreateCustomerRequest DTO validation.
 */
class CreateCustomerRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void shouldPassValidationWithValidData() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "+1234567890",
                "123 Main St",
                LocalDate.of(1990, 1, 15),
                CustomerStatus.ACTIVE
        );

        // When
        Set<ConstraintViolation<CreateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should pass validation with minimal required data")
    void shouldPassValidationWithMinimalRequiredData() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest(
                "Jane",
                "Smith",
                "jane@example.com",
                "+1234567890",
                null,  // address is optional
                null,  // dateOfBirth is optional
                null   // customerStatus is optional
        );

        // When
        Set<ConstraintViolation<CreateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation when firstName is blank")
    void shouldFailValidationWhenFirstNameIsBlank() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest(
                "",  // blank firstName
                "Doe",
                "john.doe@example.com",
                "+1234567890",
                null,
                null,
                null
        );

        // When
        Set<ConstraintViolation<CreateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("First name is required");
    }

    @Test
    @DisplayName("Should fail validation when lastName is null")
    void shouldFailValidationWhenLastNameIsNull() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest(
                "John",
                null,  // null lastName
                "john.doe@example.com",
                "+1234567890",
                null,
                null,
                null
        );

        // When
        Set<ConstraintViolation<CreateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Last name is required");
    }

    @Test
    @DisplayName("Should fail validation when firstName exceeds maximum length")
    void shouldFailValidationWhenFirstNameExceedsMaxLength() {
        // Given
        String longFirstName = "a".repeat(51);  // 51 characters
        CreateCustomerRequest request = new CreateCustomerRequest(
                longFirstName,
                "Doe",
                "john.doe@example.com",
                "+1234567890",
                null,
                null,
                null
        );

        // When
        Set<ConstraintViolation<CreateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("First name must not exceed 50 characters");
    }

    @Test
    @DisplayName("Should fail validation when email is invalid")
    void shouldFailValidationWhenEmailIsInvalid() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest(
                "John",
                "Doe",
                "invalid-email",  // invalid email format
                "+1234567890",
                null,
                null,
                null
        );

        // When
        Set<ConstraintViolation<CreateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email must be valid");
    }

    @Test
    @DisplayName("Should fail validation when phone number is invalid")
    void shouldFailValidationWhenPhoneNumberIsInvalid() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "123",  // invalid phone number (too short)
                null,
                null,
                null
        );

        // When
        Set<ConstraintViolation<CreateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Phone number must be in international format");
    }

    @Test
    @DisplayName("Should fail validation when address exceeds maximum length")
    void shouldFailValidationWhenAddressExceedsMaxLength() {
        // Given
        String longAddress = "a".repeat(201);  // 201 characters
        CreateCustomerRequest request = new CreateCustomerRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "+1234567890",
                longAddress,
                null,
                null
        );

        // When
        Set<ConstraintViolation<CreateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Address must not exceed 200 characters");
    }

    @Test
    @DisplayName("Should fail validation with multiple constraint violations")
    void shouldFailValidationWithMultipleConstraintViolations() {
        // Given
        CreateCustomerRequest request = new CreateCustomerRequest(
                "",  // blank firstName
                "",  // blank lastName
                "invalid-email",  // invalid email
                "123",  // invalid phone
                "a".repeat(201),  // address too long
                null,
                null
        );

        // When
        Set<ConstraintViolation<CreateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(5);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "First name is required",
                        "Last name is required",
                        "Email must be valid",
                        "Phone number must be in international format",
                        "Address must not exceed 200 characters"
                );
    }

    @Test
    @DisplayName("Should accept valid international phone numbers")
    void shouldAcceptValidInternationalPhoneNumbers() {
        // Test various valid phone number formats
        String[] validPhones = {
                "+1234567890",      // with +
                "1234567890",       // without +
                "+12345678901234",  // 14 digits
                "123456789012345"   // 15 digits
        };

        for (String phone : validPhones) {
            CreateCustomerRequest request = new CreateCustomerRequest(
                    "John", "Doe", "john@example.com", phone, null, null, null
            );

            Set<ConstraintViolation<CreateCustomerRequest>> violations = validator.validate(request);
            assertThat(violations)
                    .withFailMessage("Phone number %s should be valid", phone)
                    .isEmpty();
        }
    }

    @Test
    @DisplayName("Should reject invalid phone numbers")
    void shouldRejectInvalidPhoneNumbers() {
        // Test various invalid phone number formats
        String[] invalidPhones = {
                "123456789",         // too short (9 digits)
                "1234567890123456",  // too long (16 digits)
                "+123abc567890",     // contains letters
                "123-456-7890",      // contains dashes
                "(123) 456-7890"     // contains parentheses and spaces
        };

        for (String phone : invalidPhones) {
            CreateCustomerRequest request = new CreateCustomerRequest(
                    "John", "Doe", "john@example.com", phone, null, null, null
            );

            Set<ConstraintViolation<CreateCustomerRequest>> violations = validator.validate(request);
            assertThat(violations)
                    .withFailMessage("Phone number %s should be invalid", phone)
                    .hasSize(1);
        }
    }
}
