package com.bootcamp.ex.sb_exercise1.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Calculator {
  public static Calculator operate = new Calculator();

  private Calculator(){

  }

  public Calculator(String x, String y, Operation operation){
    this.x = x;
    this.y = y;
    this.operation = operation;
  }

  private String x;
  private String y;
  private Operation operation;
  private BigDecimal result;
}
