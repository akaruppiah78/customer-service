package com.example.customerservice.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for CustomerStatus enum.
 */
class CustomerStatusTest {

    @Test
    @DisplayName("Should have all expected enum values")
    void shouldHaveAllExpectedEnumValues() {
        // When
        CustomerStatus[] values = CustomerStatus.values();

        // Then
        assertThat(values).hasSize(3)
                .containsExactlyInAnyOrder(
                        CustomerStatus.ACTIVE,
                        CustomerStatus.INACTIVE,
                        CustomerStatus.SUSPENDED
                );
    }

    @Test
    @DisplayName("Should convert enum to string correctly")
    void shouldConvertEnumToStringCorrectly() {
        // Then
        assertThat(CustomerStatus.ACTIVE).hasToString("ACTIVE");
        assertThat(CustomerStatus.INACTIVE).hasToString("INACTIVE");
        assertThat(CustomerStatus.SUSPENDED).hasToString("SUSPENDED");
    }

    @Test
    @DisplayName("Should support valueOf conversion")
    void shouldSupportValueOfConversion() {
        // Then
        assertThat(CustomerStatus.valueOf("ACTIVE")).isEqualTo(CustomerStatus.ACTIVE);
        assertThat(CustomerStatus.valueOf("INACTIVE")).isEqualTo(CustomerStatus.INACTIVE);
        assertThat(CustomerStatus.valueOf("SUSPENDED")).isEqualTo(CustomerStatus.SUSPENDED);
    }

    @Test
    @DisplayName("Should have consistent ordinal values")
    void shouldHaveConsistentOrdinalValues() {
        // Then
        assertThat(CustomerStatus.ACTIVE.ordinal()).isZero();
        assertThat(CustomerStatus.INACTIVE.ordinal()).isEqualTo(1);
        assertThat(CustomerStatus.SUSPENDED.ordinal()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should support name() method")
    void shouldSupportNameMethod() {
        // Then
        assertThat(CustomerStatus.ACTIVE.name()).isEqualTo("ACTIVE");
        assertThat(CustomerStatus.INACTIVE.name()).isEqualTo("INACTIVE");
        assertThat(CustomerStatus.SUSPENDED.name()).isEqualTo("SUSPENDED");
    }

    @Test
    @DisplayName("Should demonstrate enum equality")
    void shouldDemonstrateEnumEquality() {
        // Given
        CustomerStatus status1 = CustomerStatus.ACTIVE;
        CustomerStatus status2 = CustomerStatus.valueOf("ACTIVE");

        // Then
        assertThat(status1).isEqualTo(status2)
                .isSameAs(status2) // Enums are singletons
                .hasSameHashCodeAs(status2);
    }

    @Test
    @DisplayName("Should demonstrate enum comparison")
    void shouldDemonstrateEnumComparison() {
        // Then - ordinal-based comparison
        assertThat(CustomerStatus.ACTIVE.compareTo(CustomerStatus.INACTIVE)).isLessThan(0);
        assertThat(CustomerStatus.INACTIVE.compareTo(CustomerStatus.SUSPENDED)).isLessThan(0);
        assertThat(CustomerStatus.SUSPENDED.compareTo(CustomerStatus.ACTIVE)).isGreaterThan(0);
        assertThat(CustomerStatus.ACTIVE).isEqualByComparingTo(CustomerStatus.ACTIVE);
    }
}
