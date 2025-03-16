package com.finance.project.final_project.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FiveMinDataDTO {
  @Setter
  private Long currentRegularMarketTime;
  @Setter
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime convertedMarketTime;
  @Setter
  private String symbol;
  private List<TStockPriceDTO> tStockPrices;

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  public static class TStockPriceDTO {
    private String type;
    private ZonedDateTime apiDateTime;
    private String symbol;
    private Long regularMarketTime;
    private ZonedDateTime marketTimeWithZone;
    private Double regularMarketPrice;
    private Double regularMarketChangePercent;
    @Setter
    @JsonProperty(value = "SMAFiveMins")
    private Double SMAFiveMins;
  }
  public Double calculateSMAWithFive (){
    if (this.tStockPrices.size() >= 5){
      int size = this.tStockPrices.size();
        List<Double> lastFivePrices = //
          this.tStockPrices.subList(Math.max(size - 5, 0), size) //
          .stream().map(e -> e.getRegularMarketPrice())
          .collect(Collectors.toList());
      Double total = 0.0;
      for (Double price : lastFivePrices){
        total = BigDecimal.valueOf(total).add(BigDecimal.valueOf(price)).doubleValue();
      }
      Double result = //
        BigDecimal.valueOf(total) //
        .divide(BigDecimal.valueOf(5.0), 2, RoundingMode.UP).doubleValue();
      return result;
    }
    return null;
  }
}
