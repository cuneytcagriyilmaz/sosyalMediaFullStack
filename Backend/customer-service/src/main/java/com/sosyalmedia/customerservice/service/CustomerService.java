package com.sosyalmedia.customerservice.service;


import com.sosyalmedia.customerservice.dto.CustomerRequest;
import com.sosyalmedia.customerservice.dto.CustomerResponse;
import com.sosyalmedia.customerservice.dto.CustomerListResponse;
import com.sosyalmedia.customerservice.dto.CustomerUpdateRequest;
import com.sosyalmedia.customerservice.entity.Customer;

import java.util.List;

public interface CustomerService {

    // CREATE
    CustomerResponse createCustomer(CustomerRequest request);

    // READ
    CustomerResponse getCustomerById(Long id);
    CustomerResponse getCustomerByCompanyName(String companyName);
    List<CustomerListResponse> getAllCustomers();
    List<CustomerListResponse> getCustomersBySector(String sector);
    List<CustomerListResponse> getCustomersByStatus(Customer.CustomerStatus status);

    // UPDATE (PATCH)
    CustomerResponse patchCustomer(Long id, CustomerUpdateRequest request);
    CustomerResponse patchCustomerByCompanyName(String companyName, CustomerUpdateRequest request);

    // DELETE
    void softDeleteCustomer(Long id);
    void softDeleteCustomerByCompanyName(String companyName);
    void hardDeleteCustomer(Long id);

    // RESTORE
    void restoreCustomer(Long id) throws Throwable;

    // DELETED
    List<CustomerResponse> getAllDeletedCustomers();
}