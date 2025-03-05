package com.bootcamp.demo.demo_sb_customer.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.demo.demo_sb_customer.controller.CalculatorOperation;
import com.bootcamp.demo.demo_sb_customer.dto.CalculatorResult;
import com.bootcamp.demo.demo_sb_customer.service.CalculatorService;

// @Autowired(required=false) 
// CalculatorOperation calculatorOperation;
// then method will call calculatorOperation.xxxx()

@RestController
public class CalculatorController implements CalculatorOperation{
  // Unit test for calculate: assume sum() return 100 and subtract 200 -> -100
  // (1, 2) -> 3 - (-1) -> expect 4 (OK)
  // (1, 2) -> 3 - (-2) -> expect 4 (Fail)
  @Autowired
  private CalculatorService calculatorService;

  @Override
  public CalculatorResult calculate(int x, int y){
    return new CalculatorResult(
        this.calculatorService.sum(x, y) - this.calculatorService.subtract(x, y));
  }

  // // Unit test for sum()
  // // 1 + 2 -> 3
  // public int sum(int x, int y){
  //   return x + y;
  // }

  // // Unit test for subtract()
  // // 1 - 2 -> -1
  // public int subtract(int x, int y){
  //   return x - y - 1; // NEW
  // }
}
