package com.bootcamp.demo.demo_sb_customer.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.demo.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.demo.demo_sb_customer.codewave.Syscode;
import com.bootcamp.demo.demo_sb_customer.controller.CustomerOperation;
import com.bootcamp.demo.demo_sb_customer.entity.CustomerEntity;
import com.bootcamp.demo.demo_sb_customer.service.CustomerService;
import com.bootcamp.demo.demo_sb_customer.service.impl.CustomerServiceImpl;

@RestController
public class CustomerController implements CustomerOperation {

  @Autowired
  private CustomerService customerService;

  @Override
  public ApiResp<CustomerEntity> createCustomer(CustomerEntity customerEntity){
    CustomerEntity serviceResult = this.customerService.createCustomer(customerEntity);
    return ApiResp.<CustomerEntity>builder().syscode(Syscode.OK).data(serviceResult).build();
  }

  @Override
  public ApiResp<Optional<CustomerEntity>> getCustomer(Long id){
    Optional<CustomerEntity> resultData = this.customerService.get(id);
    if (resultData.isPresent())
      return ApiResp.<Optional<CustomerEntity>>builder().syscode(Syscode.OK).data(resultData).build();
    return ApiResp.<Optional<CustomerEntity>>builder().syscode(Syscode.ID_NOT_FOUND).data(resultData).build();
  }

  @Override
  public ApiResp<List<CustomerEntity>> getCustomers(){
    List<CustomerEntity> customerEntities = this.customerService.getCustomers();
    return ApiResp.<List<CustomerEntity>>builder() //
    .syscode(Syscode.OK).data(customerEntities).build();
  }

  @Override
  public List<CustomerEntity> findByName(String name){
    return this.customerService.findByName(name);
  }

  @Override
  public List<CustomerEntity> findByNameByJPQL(String name){
    return this.customerService.findByNameByJPQL(name);
  }

  @Override
  public List<CustomerEntity> findByNameByNativeQuery(@RequestParam String name){
    return this.customerService.findByNameByNativeQuery(name);
  }
}
