package com.finance.project.final_project.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Setter
public class StockOHLCDataDTO {
  private Long regularMarketTime;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime convertedDateTime;
  private String symbol;
  private String type;
  private List<StockOHLCDTO> stockOHLCs;

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class StockOHLCDTO {
    private Long regularMarketTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate convertedDate;
    private String symbol;
    private String type;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    @JsonProperty(value = "FiveSMA")
    private Double FiveSMA;
    @JsonProperty(value = "TenSMA")
    private Double TenSMA;
    @JsonProperty(value = "TwentySMA")
    private Double twentySMA;
  }
  public void calFiveSMA() {
    for (int i = this.stockOHLCs.size() - 1; i >= 0; i--){
      Double sum = 0.0;
      Double average = 0.0;
      if (i - 5 >= 0){
        List<Double> found = this.stockOHLCs.subList(i - 5, i).stream() //
          .map(e -> e.getClose()).collect(Collectors.toList());
        for (Double d : found){
          sum = BigDecimal.valueOf(sum).add(BigDecimal.valueOf(d)).doubleValue();
        }
        average = //
        BigDecimal.valueOf(sum) //
        .divide(BigDecimal.valueOf(5), 2, RoundingMode.UP).doubleValue();
        this.stockOHLCs.get(i).setFiveSMA(average);
      }
    }
  }

  public void calTenSMA() {
    for (int i = this.stockOHLCs.size() - 1; i >= 0; i--){
      Double sum = 0.0;
      Double average = 0.0;
      if (i - 10 >= 0){
        List<Double> found = this.stockOHLCs.subList(i - 10, i).stream() //
          .map(e -> e.getClose()).collect(Collectors.toList());
        for (Double d : found){
          sum = BigDecimal.valueOf(sum).add(BigDecimal.valueOf(d)).doubleValue();
        }
        average = //
        BigDecimal.valueOf(sum) //
        .divide(BigDecimal.valueOf(10), 2, RoundingMode.UP).doubleValue();
        this.stockOHLCs.get(i).setTenSMA(average);
      }
    }
  }

  public void calTwentySMA() {
    for (int i = this.stockOHLCs.size() - 1; i >= 0; i--){
      Double sum = 0.0;
      Double average = 0.0;
      if (i - 20 >= 0){
        List<Double> found = this.stockOHLCs.subList(i - 20, i).stream() //
          .map(e -> e.getClose()).collect(Collectors.toList());
        for (Double d : found){
          sum = BigDecimal.valueOf(sum).add(BigDecimal.valueOf(d)).doubleValue();
        }
        average = //
        BigDecimal.valueOf(sum) //
        .divide(BigDecimal.valueOf(20), 2, RoundingMode.UP).doubleValue();
        this.stockOHLCs.get(i).setTwentySMA(average);
      }
    }
  }
}
