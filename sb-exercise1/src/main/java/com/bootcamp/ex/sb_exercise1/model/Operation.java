package com.bootcamp.ex.sb_exercise1.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Operation {
  private String x;
  private String y;
  private String operation;
  private BigDecimal result;
}
