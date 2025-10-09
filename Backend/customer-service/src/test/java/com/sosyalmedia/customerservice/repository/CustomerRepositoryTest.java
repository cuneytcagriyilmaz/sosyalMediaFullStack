package com.sosyalmedia.customerservice.repository;

import com.sosyalmedia.customerservice.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void saveAndFindById_Success() {
        Customer customer = Customer.builder()
                .companyName("Test Cafe")
                .sector("cafe")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .deleted(false)
                .build();

        Customer saved = customerRepository.save(customer);

        assertThat(saved.getId()).isNotNull();

        Optional<Customer> found = customerRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getCompanyName()).isEqualTo("Test Cafe");
    }

    @Test
    void findByCompanyName_Success() {
        Customer customer = Customer.builder()
                .companyName("Unique Cafe")
                .sector("cafe")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .deleted(false)
                .build();

        customerRepository.save(customer);

        Optional<Customer> found = customerRepository.findByCompanyName("Unique Cafe");

        assertThat(found).isPresent();
        assertThat(found.get().getCompanyName()).isEqualTo("Unique Cafe");
    }

    @Test
    void softDelete_CustomerNotReturned() {
        Customer customer = Customer.builder()
                .companyName("To Delete")
                .sector("cafe")
                .address("Test Address")
                .membershipPackage("Gold")
                .status(Customer.CustomerStatus.ACTIVE)
                .deleted(false)
                .build();

        Customer saved = customerRepository.save(customer);
        customerRepository.delete(saved); // Soft delete

        Optional<Customer> found = customerRepository.findById(saved.getId());

        assertThat(found).isEmpty(); // SQLRestriction sayesinde bulunamaz
    }
}