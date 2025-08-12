package com.example.customerservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.Instant;
import java.util.Objects;

/**
 * Customer entity representing a customer in the system.
 * 
 * <p>This class maps to the "customers" collection in MongoDB and contains all customer information
 * including personal details, contact information, and audit fields.</p>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Automatic ID generation using MongoDB ObjectId</li>
 *   <li>Email uniqueness enforcement through database index</li>
 *   <li>Phone number validation in international format</li>
 *   <li>Automatic audit fields (createdAt, updatedAt) via MongoDB auditing</li>
 *   <li>Customer status management (ACTIVE, INACTIVE, SUSPENDED)</li>
 * </ul>
 * 
 * @author Customer Service Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Document(collection = "customers")
public class Customer {
    
    /**
     * Unique identifier for the customer (MongoDB ObjectId).
     * Generated automatically by MongoDB when not provided.
     */
    @Id
    private String customerId;
    
    /**
     * Customer's first name.
     * Required field with maximum length of 50 characters.
     */
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;
    
    /**
     * Customer's last name.
     * Required field with maximum length of 50 characters.
     */
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;
    
    /**
     * Customer's email address.
     * Must be unique across all customers and follow valid email format.
     * Indexed in MongoDB for fast lookups and uniqueness enforcement.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Indexed(unique = true)
    private String email;
    
    /**
     * Customer's phone number in international format.
     * Must be 10-15 digits and may include a '+' prefix.
     * Indexed in MongoDB for fast lookups.
     */
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Phone number must be in international format")
    @Indexed
    private String phone;
    
    /**
     * Customer's address (optional).
     * Maximum length of 200 characters.
     */
    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String address;
    
    /**
     * Customer's date of birth.
     * Optional field for age verification and personalization.
     */
    private LocalDate dateOfBirth;
    
    /**
     * Current status of the customer account.
     * Defaults to ACTIVE when not specified.
     * Indexed in MongoDB for efficient status-based queries.
     */
    @Indexed
    private CustomerStatus customerStatus = CustomerStatus.ACTIVE;
    
    /**
     * Timestamp when the customer record was created.
     * Automatically populated by MongoDB auditing.
     */
    @CreatedDate
    private Instant createdAt;
    
    /**
     * Timestamp when the customer record was last modified.
     * Automatically updated by MongoDB auditing on any change.
     */
    @LastModifiedDate
    private Instant updatedAt;
    
    /**
     * Default constructor for JPA/MongoDB.
     */
    public Customer() {}
    
    /**
     * Constructor for creating a new customer with basic information.
     * 
     * @param firstName the customer's first name
     * @param lastName the customer's last name
     * @param email the customer's email address (must be unique)
     * @param phone the customer's phone number in international format
     */
    public Customer(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.customerStatus = CustomerStatus.ACTIVE;
    }
    
    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }
    
    public void setCustomerStatus(CustomerStatus customerStatus) {
        this.customerStatus = customerStatus;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", customerStatus=" + customerStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
