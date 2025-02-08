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

  private String x;
  private String y;
  private Operation operation;
  private BigDecimal result;
}
