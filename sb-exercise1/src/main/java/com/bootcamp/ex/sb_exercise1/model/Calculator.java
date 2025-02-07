package com.bootcamp.ex.sb_exercise1.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Calculator {
  public static final Operation operation = new Operation();
}
