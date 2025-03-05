package com.bootcamp.demo.demo_sb_customer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bootcamp.demo.demo_sb_customer.dto.CalculatorResult;

public interface CalculatorOperation {
  @GetMapping(value = "/calculate/{x}/{y}")
  CalculatorResult calculate(@PathVariable int x,@PathVariable int y);
}
