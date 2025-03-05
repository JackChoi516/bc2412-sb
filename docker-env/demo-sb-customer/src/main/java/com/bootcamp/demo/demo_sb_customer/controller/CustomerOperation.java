package com.bootcamp.demo.demo_sb_customer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bootcamp.demo.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.demo.demo_sb_customer.entity.CustomerEntity;

public interface CustomerOperation {
  
  @PostMapping(value = "/customer")
  @ResponseStatus(HttpStatus.CREATED) // 201
  ApiResp<CustomerEntity> createCustomer(@RequestBody CustomerEntity customereEntity);

  @GetMapping(value = "/customer")
  ApiResp<Optional<CustomerEntity>> getCustomer(@RequestParam Long id);

  @GetMapping(value = "/customers")
  ApiResp<List<CustomerEntity>> getCustomers();

  @GetMapping(value = "/customer/findbyname")
  List<CustomerEntity> findByName(@RequestParam String name);

  @GetMapping(value = "/customer/findbyname/jpql")
  List<CustomerEntity> findByNameByJPQL(@RequestParam String name);

  @GetMapping(value = "/customer/findbyname/nativequery")
  List<CustomerEntity> findByNameByNativeQuery(@RequestParam String name);
}
