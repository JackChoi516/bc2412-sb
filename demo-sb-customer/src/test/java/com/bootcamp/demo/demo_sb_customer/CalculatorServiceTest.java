package com.bootcamp.demo.demo_sb_customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bootcamp.demo.demo_sb_customer.service.CalculatorService;

@SpringBootTest
public class CalculatorServiceTest {
  @Autowired
  private CalculatorService calculatorService;
  
  @Test
  void testSum(){
    assertEquals(3, this.calculatorService.sum(1, 2));
  }

  @Test
  void testSubtract(){
    assertEquals(-2, this.calculatorService.subtract(1, 2));
  }
}
