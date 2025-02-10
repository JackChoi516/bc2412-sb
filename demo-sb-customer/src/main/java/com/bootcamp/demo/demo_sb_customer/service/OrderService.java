package com.bootcamp.demo.demo_sb_customer.service;

import com.bootcamp.demo.demo_sb_customer.entity.OrderEntity;

public interface OrderService {
  OrderEntity createOrder(Long customerId, OrderEntity orderEntity);
}
