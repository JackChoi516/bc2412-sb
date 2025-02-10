package com.bootcamp.demo.demo_sb_customer.service;

import java.util.List;
import java.util.Optional;

import com.bootcamp.demo.demo_sb_customer.entity.CustomerEntity;

public interface CustomerService {
  CustomerEntity createCustomer (CustomerEntity customerEntity);
  Optional<CustomerEntity> get (Long id);
  List<CustomerEntity> getCustomers();
  
}
