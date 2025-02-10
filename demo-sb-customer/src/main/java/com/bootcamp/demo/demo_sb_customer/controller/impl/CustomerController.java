package com.bootcamp.demo.demo_sb_customer.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.demo.demo_sb_customer.controller.CustomerOperation;
import com.bootcamp.demo.demo_sb_customer.entity.CustomerEntity;
import com.bootcamp.demo.demo_sb_customer.service.impl.CustomerServiceImpl;

@RestController
public class CustomerController implements CustomerOperation {

  @Autowired
  private CustomerServiceImpl customerServiceImpl;

  @Override
  public CustomerEntity createCustomer(CustomerEntity customerEntity){
    return this.customerServiceImpl.createCustomer(customerEntity);
  }

  @Override
  public Optional<CustomerEntity> getCustomer(Long id){
    return this.customerServiceImpl.get(id);
  }

  @Override
  public List<CustomerEntity> getCustomers(){
    return this.customerServiceImpl.getCustomers();
  }
  
}
