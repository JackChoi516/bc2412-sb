package com.bootcamp.demo.demo_sb_customer.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.demo.demo_sb_customer.controller.OrderOperation;
import com.bootcamp.demo.demo_sb_customer.entity.OrderEntity;
import com.bootcamp.demo.demo_sb_customer.service.impl.OrderServiceImpl;

@RestController
public class OrderController implements OrderOperation {
  
  @Autowired
  private OrderServiceImpl orderServiceImpl;

  @Override
  public OrderEntity createOrder (Long id, OrderEntity orderEntity){
    return this.orderServiceImpl.createOrder(id, orderEntity);
  }
}
