package com.example.customerservice.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for Customer model class.
 */
class CustomerTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should create Customer using default constructor")
    void shouldCreateCustomerUsingDefaultConstructor() {
        // When
        Customer customer = new Customer();

        // Then
        assertThat(customer).isNotNull();
        assertThat(customer.getCustomerId()).isNull();
        assertThat(customer.getFirstName()).isNull();
        assertThat(customer.getLastName()).isNull();
        assertThat(customer.getEmail()).isNull();
        assertThat(customer.getPhone()).isNull();
        assertThat(customer.getAddress()).isNull();
        assertThat(customer.getDateOfBirth()).isNull();
        assertThat(customer.getCustomerStatus()).isEqualTo(CustomerStatus.ACTIVE); // default value
        assertThat(customer.getCreatedAt()).isNull();
        assertThat(customer.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should create Customer using parameterized constructor")
    void shouldCreateCustomerUsingParameterizedConstructor() {
        // When
        Customer customer = new Customer("John", "Doe", "john.doe@example.com", "+1234567890");

        // Then
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstName()).isEqualTo("John");
        assertThat(customer.getLastName()).isEqualTo("Doe");
        assertThat(customer.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(customer.getPhone()).isEqualTo("+1234567890");
        assertThat(customer.getCustomerStatus()).isEqualTo(CustomerStatus.ACTIVE); // default
        assertThat(customer.getAddress()).isNull();
        assertThat(customer.getDateOfBirth()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllPropertiesCorrectly() {
        // Given
        Customer customer = new Customer();
        String customerId = "507f1f77bcf86cd799439011";
        String firstName = "Alice";
        String lastName = "Johnson";
        String email = "alice.johnson@example.com";
        String phone = "+9876543210";
        String address = "456 Oak Ave";
        LocalDate dateOfBirth = LocalDate.of(1985, 3, 20);
        CustomerStatus status = CustomerStatus.INACTIVE;
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now().plusSeconds(10);

        // When
        customer.setCustomerId(customerId);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setDateOfBirth(dateOfBirth);
        customer.setCustomerStatus(status);
        customer.setCreatedAt(createdAt);
        customer.setUpdatedAt(updatedAt);

        // Then
        assertThat(customer.getCustomerId()).isEqualTo(customerId);
        assertThat(customer.getFirstName()).isEqualTo(firstName);
        assertThat(customer.getLastName()).isEqualTo(lastName);
        assertThat(customer.getEmail()).isEqualTo(email);
        assertThat(customer.getPhone()).isEqualTo(phone);
        assertThat(customer.getAddress()).isEqualTo(address);
        assertThat(customer.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(customer.getCustomerStatus()).isEqualTo(status);
        assertThat(customer.getCreatedAt()).isEqualTo(createdAt);
        assertThat(customer.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should pass validation with valid customer data")
    void shouldPassValidationWithValidCustomerData() {
        // Given
        Customer customer = new Customer();
        customer.setFirstName("Valid");
        customer.setLastName("Customer");
        customer.setEmail("valid@example.com");
        customer.setPhone("+1234567890");
        customer.setAddress("123 Valid St");

        // When
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation with invalid data")
    void shouldFailValidationWithInvalidData() {
        // Given
        Customer customer = new Customer();
        customer.setFirstName(""); // blank
        customer.setLastName(""); // blank
        customer.setEmail("invalid-email"); // invalid format
        customer.setPhone("123"); // invalid format
        customer.setAddress("a".repeat(201)); // too long

        // When
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

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
    @DisplayName("Should demonstrate equals method based on customerId")
    void shouldDemonstrateEqualsMethodBasedOnCustomerId() {
        // Given
        Customer customer1 = new Customer();
        customer1.setCustomerId("123");
        customer1.setFirstName("John");
        customer1.setLastName("Doe");

        Customer customer2 = new Customer();
        customer2.setCustomerId("123");
        customer2.setFirstName("Jane"); // different name
        customer2.setLastName("Smith"); // different name

        Customer customer3 = new Customer();
        customer3.setCustomerId("456"); // different ID
        customer3.setFirstName("John");
        customer3.setLastName("Doe");

        // Then
        assertThat(customer1).isEqualTo(customer2) // same ID
                .isNotEqualTo(customer3) // different ID
                .hasSameHashCodeAs(customer2); // same hash
        assertThat(customer1.hashCode()).isNotEqualTo(customer3.hashCode()); // different hash
    }

    @Test
    @DisplayName("Should handle null customerId in equals method")
    void shouldHandleNullCustomerIdInEqualsMethod() {
        // Given
        Customer customer1 = new Customer();
        customer1.setCustomerId(null);

        Customer customer2 = new Customer();
        customer2.setCustomerId(null);

        Customer customer3 = new Customer();
        customer3.setCustomerId("123");

        // Then
        assertThat(customer1).isEqualTo(customer2) // both null IDs
                .isNotEqualTo(customer3); // null vs non-null
    }

    @Test
    @DisplayName("Should demonstrate equals with same object reference")
    void shouldDemonstrateEqualsWithSameObjectReference() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId("123");

        // Then
        assertThat(customer).isEqualTo(customer); // same reference
    }

    @Test
    @DisplayName("Should demonstrate equals with null object")
    void shouldDemonstrateEqualsWithNullObject() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId("123");

        // Then
        assertThat(customer).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should demonstrate equals with different class")
    void shouldDemonstrateEqualsWithDifferentClass() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId("123");
        String differentClass = "Not a Customer";

        // Then
        assertThat(customer).isNotEqualTo(differentClass);
    }

    @Test
    @DisplayName("Should demonstrate toString method")
    void shouldDemonstrateToStringMethod() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId("123");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("+1234567890");
        customer.setCustomerStatus(CustomerStatus.ACTIVE);
        customer.setCreatedAt(Instant.parse("2023-01-01T10:00:00Z"));
        customer.setUpdatedAt(Instant.parse("2023-01-01T10:00:00Z"));

        // When
        String toString = customer.toString();

        // Then
        assertThat(toString).contains("Customer{")
                .contains("customerId='123'")
                .contains("firstName='John'")
                .contains("lastName='Doe'")
                .contains("email='john.doe@example.com'")
                .contains("phone='+1234567890'")
                .contains("customerStatus=ACTIVE")
                .contains("createdAt=2023-01-01T10:00:00Z")
                .contains("updatedAt=2023-01-01T10:00:00Z");
    }

    @Test
    @DisplayName("Should demonstrate toString with null fields")
    void shouldDemonstrateToStringWithNullFields() {
        // Given
        Customer customer = new Customer();

        // When
        String toString = customer.toString();

        // Then
        assertThat(toString).contains("Customer{")
                .contains("customerId='null'")
                .contains("firstName='null'")
                .contains("customerStatus=ACTIVE"); // default value
    }

    @Test
    @DisplayName("Should handle all CustomerStatus values")
    void shouldHandleAllCustomerStatusValues() {
        // Test setting all enum values
        for (CustomerStatus status : CustomerStatus.values()) {
            Customer customer = new Customer();
            customer.setCustomerStatus(status);

            assertThat(customer.getCustomerStatus()).isEqualTo(status);
        }
    }

    @Test
    @DisplayName("Should accept valid phone number formats")
    void shouldAcceptValidPhoneNumberFormats() {
        // Test various valid phone number formats
        String[] validPhones = {
                "+1234567890",
                "1234567890",
                "+12345678901234",
                "123456789012345"
        };

        for (String phone : validPhones) {
            Customer customer = new Customer();
            customer.setFirstName("Test");
            customer.setLastName("User");
            customer.setEmail("test@example.com");
            customer.setPhone(phone);

            Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
            assertThat(violations)
                    .withFailMessage("Phone %s should be valid", phone)
                    .isEmpty();
        }
    }
}
