package com.bootcamp.ex.sb_exercise1.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.bootcamp.ex.sb_exercise1.model.Calculator;
import com.bootcamp.ex.sb_exercise1.model.Operation;

@Service
public class CalculatorService {
  public Calculator operate(String x, String y, Operation operation){
    Calculator.operate.setX(x);
    Calculator.operate.setY(y);
    Calculator.operate.setOperation(operation);
    Calculator.operate.setResult(switch (operation) {
    case ADD -> add(x, y);
    case SUB -> sub(x, y);
    case MUL -> mul(x, y);
    case DIV -> div(x, y);
   });
   return Calculator.operate;
  }

  public static BigDecimal add(String x, String y){
    return BigDecimal.valueOf(Double.parseDouble(x)) //
      .add(BigDecimal.valueOf(Double.parseDouble(y)));
  }

  public static BigDecimal sub(String x, String y){
    return BigDecimal.valueOf(Double.parseDouble(x)) //
      .subtract(BigDecimal.valueOf(Double.parseDouble(y)));
  }

  public static BigDecimal mul(String x, String y){
    return BigDecimal.valueOf(Double.parseDouble(x)) //
      .multiply(BigDecimal.valueOf(Double.parseDouble(y)));
  }

  public static BigDecimal div(String x, String y){
    return BigDecimal.valueOf(Double.parseDouble(x)) //
      .divide(BigDecimal.valueOf(Double.parseDouble(y)), 5, RoundingMode.HALF_UP);
  }
}
