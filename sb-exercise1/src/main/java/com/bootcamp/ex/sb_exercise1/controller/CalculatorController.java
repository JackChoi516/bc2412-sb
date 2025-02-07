package com.bootcamp.ex.sb_exercise1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bootcamp.ex.sb_exercise1.model.Operation;
import com.bootcamp.ex.sb_exercise1.service.CalculatorService;

@RestController
public class CalculatorController {
  
  @Autowired
  private CalculatorService calculatorService;

  @GetMapping(value = "/operation")
  public Operation operation(@RequestParam String x, @RequestParam String y, @RequestParam String operation){
    return this.calculatorService.operate(x, y, operation);
  }
}
