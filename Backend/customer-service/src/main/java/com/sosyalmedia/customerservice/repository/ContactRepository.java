// src/main/java/com/sosyalmedia/customerservice/repository/ContactRepository.java

package com.sosyalmedia.customerservice.repository;

import com.sosyalmedia.customerservice.entity.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<CustomerContact, Long> {
}