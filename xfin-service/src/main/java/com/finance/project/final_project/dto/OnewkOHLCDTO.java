package com.finance.project.final_project.dto;

import java.time.LocalDateTime;

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
public class OnewkOHLCDTO {
  private Long regularMarketTime;
  private LocalDateTime convertedDateTime;
  private String symbol;
  private String type;
  private Double open;
  private Double close;
  private Double high;
  private Double low;
}
