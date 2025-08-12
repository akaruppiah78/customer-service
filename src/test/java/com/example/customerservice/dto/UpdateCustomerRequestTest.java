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
 * Unit tests for UpdateCustomerRequest DTO validation.
 */
class UpdateCustomerRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should pass validation with all valid fields")
    void shouldPassValidationWithAllValidFields() {
        // Given
        UpdateCustomerRequest request = new UpdateCustomerRequest(
                "UpdatedJohn",
                "UpdatedDoe",
                "updated.john@example.com",
                "+9876543210",
                "456 Updated St",
                LocalDate.of(1995, 6, 20),
                CustomerStatus.INACTIVE
        );

        // When
        Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should pass validation with all null fields")
    void shouldPassValidationWithAllNullFields() {
        // Given - all fields are null (partial update scenario)
        UpdateCustomerRequest request = new UpdateCustomerRequest(
                null, null, null, null, null, null, null
        );

        // When
        Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should pass validation with partial fields")
    void shouldPassValidationWithPartialFields() {
        // Given
        UpdateCustomerRequest request = new UpdateCustomerRequest(
                "PartialUpdate",
                null,  // lastName not updated
                null,  // email not updated
                "+5555555555",
                null,  // address not updated
                LocalDate.of(1988, 12, 1),
                null   // status not updated
        );

        // When
        Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation when firstName exceeds maximum length")
    void shouldFailValidationWhenFirstNameExceedsMaxLength() {
        // Given
        String longFirstName = "a".repeat(51);  // 51 characters
        UpdateCustomerRequest request = new UpdateCustomerRequest(
                longFirstName,
                null, null, null, null, null, null
        );

        // When
        Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("First name must not exceed 50 characters");
    }

    @Test
    @DisplayName("Should fail validation when lastName exceeds maximum length")
    void shouldFailValidationWhenLastNameExceedsMaxLength() {
        // Given
        String longLastName = "b".repeat(51);  // 51 characters
        UpdateCustomerRequest request = new UpdateCustomerRequest(
                null,
                longLastName,
                null, null, null, null, null
        );

        // When
        Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Last name must not exceed 50 characters");
    }

    @Test
    @DisplayName("Should fail validation when email format is invalid")
    void shouldFailValidationWhenEmailFormatIsInvalid() {
        // Given
        UpdateCustomerRequest request = new UpdateCustomerRequest(
                null, null,
                "invalid-email-format",  // invalid email
                null, null, null, null
        );

        // When
        Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email must be valid");
    }

    @Test
    @DisplayName("Should fail validation when phone number format is invalid")
    void shouldFailValidationWhenPhoneNumberFormatIsInvalid() {
        // Given
        UpdateCustomerRequest request = new UpdateCustomerRequest(
                null, null, null,
                "invalid-phone",  // invalid phone
                null, null, null
        );

        // When
        Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Phone number must be in international format");
    }

    @Test
    @DisplayName("Should fail validation when address exceeds maximum length")
    void shouldFailValidationWhenAddressExceedsMaxLength() {
        // Given
        String longAddress = "c".repeat(201);  // 201 characters
        UpdateCustomerRequest request = new UpdateCustomerRequest(
                null, null, null, null,
                longAddress,
                null, null
        );

        // When
        Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Address must not exceed 200 characters");
    }

    @Test
    @DisplayName("Should fail validation with multiple constraint violations")
    void shouldFailValidationWithMultipleConstraintViolations() {
        // Given
        UpdateCustomerRequest request = new UpdateCustomerRequest(
                "a".repeat(51),          // firstName too long
                "b".repeat(51),          // lastName too long
                "invalid-email",         // invalid email
                "invalid-phone",         // invalid phone
                "c".repeat(201),         // address too long
                LocalDate.of(2025, 1, 1), // valid date
                CustomerStatus.ACTIVE    // valid status
        );

        // When
        Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(5);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "First name must not exceed 50 characters",
                        "Last name must not exceed 50 characters",
                        "Email must be valid",
                        "Phone number must be in international format",
                        "Address must not exceed 200 characters"
                );
    }

    @Test
    @DisplayName("Should accept various valid email formats")
    void shouldAcceptVariousValidEmailFormats() {
        // Test various valid email formats
        String[] validEmails = {
                "test@example.com",
                "user.name@domain.co.uk",
                "user+tag@example.org",
                "123@numbers.com",
                "test-email@test-domain.com"
        };

        for (String email : validEmails) {
            UpdateCustomerRequest request = new UpdateCustomerRequest(
                    null, null, email, null, null, null, null
            );

            Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);
            assertThat(violations)
                    .withFailMessage("Email %s should be valid", email)
                    .isEmpty();
        }
    }

    @Test
    @DisplayName("Should accept various valid phone number formats")
    void shouldAcceptVariousValidPhoneNumberFormats() {
        // Test various valid phone number formats
        String[] validPhones = {
                "+1234567890",
                "1234567890",
                "+12345678901234",
                "123456789012345"
        };

        for (String phone : validPhones) {
            UpdateCustomerRequest request = new UpdateCustomerRequest(
                    null, null, null, phone, null, null, null
            );

            Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);
            assertThat(violations)
                    .withFailMessage("Phone number %s should be valid", phone)
                    .isEmpty();
        }
    }

    @Test
    @DisplayName("Should accept all customer status values")
    void shouldAcceptAllCustomerStatusValues() {
        // Test all enum values
        for (CustomerStatus status : CustomerStatus.values()) {
            UpdateCustomerRequest request = new UpdateCustomerRequest(
                    null, null, null, null, null, null, status
            );

            Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);
            assertThat(violations)
                    .withFailMessage("Status %s should be valid", status)
                    .isEmpty();
        }
    }

    @Test
    @DisplayName("Should handle edge case with exactly maximum field lengths")
    void shouldHandleEdgeCaseWithExactlyMaximumFieldLengths() {
        // Given - exactly at the limits
        String firstName50 = "a".repeat(50);    // exactly 50 characters
        String lastName50 = "b".repeat(50);     // exactly 50 characters
        String address200 = "c".repeat(200);    // exactly 200 characters

        UpdateCustomerRequest request = new UpdateCustomerRequest(
                firstName50,
                lastName50,
                "valid@example.com",
                "+1234567890",
                address200,
                LocalDate.of(1990, 1, 1),
                CustomerStatus.ACTIVE
        );

        // When
        Set<ConstraintViolation<UpdateCustomerRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }
}
