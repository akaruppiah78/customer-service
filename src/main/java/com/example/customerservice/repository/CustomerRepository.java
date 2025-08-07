package com.example.customerservice.repository;

import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    
    Optional<Customer> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByEmailAndCustomerIdNot(String email, String customerId);
    
    Page<Customer> findByCustomerStatus(CustomerStatus customerStatus, Pageable pageable);
    
    Page<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstName, String lastName, Pageable pageable);
}
