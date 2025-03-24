package com.bootcamp.demo_swagger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StockDTO {
  private String symbol;
  private String description;
}
