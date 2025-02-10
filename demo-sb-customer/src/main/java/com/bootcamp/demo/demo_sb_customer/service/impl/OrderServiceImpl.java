package com.bootcamp.demo.demo_sb_customer.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.demo.demo_sb_customer.entity.CustomerEntity;
import com.bootcamp.demo.demo_sb_customer.entity.OrderEntity;
import com.bootcamp.demo.demo_sb_customer.exception.BusinessException;
import com.bootcamp.demo.demo_sb_customer.repository.CustomerRepository;
import com.bootcamp.demo.demo_sb_customer.repository.OrderRepository;
import com.bootcamp.demo.demo_sb_customer.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private CustomerRepository customerRepository;

  // if customerId not found, throw new BusinessException("Customer ID not found.")
  @Override
  public OrderEntity createOrder(Long customerId, OrderEntity orderEntity){
    CustomerEntity customerEntity = this.customerRepository.findById(customerId) //
      .orElseThrow(() -> new BusinessException("Customer ID not Found."));
    orderEntity.setCustomerEntity(customerEntity);   
    return this.orderRepository.save(orderEntity);
  }

}
