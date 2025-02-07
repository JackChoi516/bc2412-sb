package com.bootcamp.ex.sb_exercise1.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.bootcamp.ex.sb_exercise1.model.Calculator;
import com.bootcamp.ex.sb_exercise1.model.Operation;

@Service
public class CalculatorService {
  public Operation operate(String x, String y, String operation){
    Calculator.operation.setX(x);
    Calculator.operation.setY(y);
    Calculator.operation.setOperation(operation);
    if (Calculator.operation.getOperation().equals("add")){
      Calculator.operation.setResult(
        new BigDecimal(Calculator.operation.getX()) //
        .add(new BigDecimal(Calculator.operation.getY()))
      );
    }
    return Calculator.operation;
  }
}
