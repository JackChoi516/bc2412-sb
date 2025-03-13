package com.finance.project.final_project.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private List<TStockPriceDTO> data;
    private List<Double> SMAFiveMins;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class TStockPriceDTO {
      private String type;
      private ZonedDateTime apiDateTime;
      @Setter
      private String symbol;
      private Long regularMarketTime;
      private ZonedDateTime marketTimeWithZone;
      private Double regularMarketPrice;
      private Double regularMarketChangePercent;
      private Double bid;
      private Integer bidSize;
      private Double ask;
      private Integer askSize;
    }
  }
}
