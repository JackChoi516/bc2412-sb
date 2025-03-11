package com.finance.project.final_project.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
// @JsonIgnoreProperties(ignoreUnknown = true)
public class FiveMinDataDTO {
  private Map<String, TimeAndData> dataRetrieved;

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  public static class TimeAndData {
    private String regularMarketTime;
    private List<QuoteData> data;
    
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class QuoteData {
      private String symbol;
      private String regularMarketTime;
      private Double regularMarketPrice;
      private Double regularMarketChangePercent;
    }
  }
}
