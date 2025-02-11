package com.bootcamp.demo.demo_sb_customer.controller.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.demo.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.demo.demo_sb_customer.codewave.Syscode;
import com.bootcamp.demo.demo_sb_customer.controller.CustomerOperation;
import com.bootcamp.demo.demo_sb_customer.entity.CustomerEntity;
import com.bootcamp.demo.demo_sb_customer.service.impl.CustomerServiceImpl;

@RestController
public class CustomerController implements CustomerOperation {

  @Autowired
  private CustomerServiceImpl customerServiceImpl;

  @Override
  public ApiResp<CustomerEntity> createCustomer(CustomerEntity customerEntity){
    CustomerEntity serviceResult = this.customerServiceImpl.createCustomer(customerEntity);
    return ApiResp.<CustomerEntity>builder().syscode(Syscode.OK).data(serviceResult).build();
  }

  @Override
  public ApiResp<Optional<CustomerEntity>> getCustomer(Long id){
    Optional<CustomerEntity> resultData = this.customerServiceImpl.get(id);
    if (resultData.isPresent())
      return ApiResp.<Optional<CustomerEntity>>builder().syscode(Syscode.OK).data(resultData).build();
    return ApiResp.<Optional<CustomerEntity>>builder().syscode(Syscode.ID_NOT_FOUND).data(resultData).build();
  }

  @Override
  public ApiResp<List<CustomerEntity>> getCustomers(){
    List<CustomerEntity> customerEntities = this.customerServiceImpl.getCustomers();
    return ApiResp.<List<CustomerEntity>>builder() //
    .syscode(Syscode.OK).data(customerEntities).build();
  }
  
}
