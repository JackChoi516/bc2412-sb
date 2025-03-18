package com.finance.project.final_project.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockOHLCDTO {
  private Long regularMarketTime;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate convertedDate;
  private String symbol;
  private String type;
  private Double open;
  private Double close;
  private Double high;
  private Double low;
}
