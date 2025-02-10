package com.bootcamp.demo.demo_sb_customer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bootcamp.demo.demo_sb_customer.entity.CustomerEntity;

public interface CustomerOperation {
  
  @PostMapping(value = "/customer")
  public CustomerEntity createCustomer(@RequestBody CustomerEntity customereEntity);

  @GetMapping(value = "/customer")
  public Optional<CustomerEntity> getCustomer(@RequestParam Long id);

  @GetMapping(value = "/customers")
  public List<CustomerEntity> getCustomers();
}
