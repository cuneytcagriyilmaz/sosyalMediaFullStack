package com.sosyalmedia.customerservice.repository;

import com.sosyalmedia.customerservice.entity.CustomerMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerMediaRepository extends JpaRepository<CustomerMedia, Long> {
    List<CustomerMedia> findByCustomerId(Long customerId);
}