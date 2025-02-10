package com.bootcamp.demo.demo_sb_customer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bootcamp.demo.demo_sb_customer.entity.OrderEntity;

// localhost:8100/order?cid=2
public interface OrderOperation {
  @PostMapping(value = "/order")
  OrderEntity createOrder(@RequestParam(value = "cid") Long customerId, 
      @RequestBody OrderEntity orderEntity);
}

