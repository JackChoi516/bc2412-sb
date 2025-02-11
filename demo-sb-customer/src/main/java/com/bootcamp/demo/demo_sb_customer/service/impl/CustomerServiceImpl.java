package com.bootcamp.demo.demo_sb_customer.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.demo.demo_sb_customer.entity.CustomerEntity;
import com.bootcamp.demo.demo_sb_customer.repository.CustomerRepository;
import com.bootcamp.demo.demo_sb_customer.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  private CustomerRepository customerRepository;
  

  @Override
  public CustomerEntity createCustomer(CustomerEntity customerEntity){
    return this.customerRepository.save(customerEntity);
  }

  @Override
  public Optional<CustomerEntity> get(Long id){
    return this.customerRepository.findById(id);
  }

  @Override
  public List<CustomerEntity> getCustomers(){
    return this.customerRepository.findAll();
  }
}
