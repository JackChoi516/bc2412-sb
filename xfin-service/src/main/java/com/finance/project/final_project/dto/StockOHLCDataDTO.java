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
  public void setFiveSMA(int interval) {
    for (int i = this.stockOHLCs.size() - 1; i >= 0; i--){
      Double sum = 0.0;
      Double average = 0.0;
      if (i - interval >= 0){
        List<Double> found = this.stockOHLCs.subList(i - interval, i).stream() //
          .map(e -> e.getClose()).collect(Collectors.toList());
        for (Double d : found){
          sum = BigDecimal.valueOf(sum).add(BigDecimal.valueOf(d)).doubleValue();
        }
        average = //
        BigDecimal.valueOf(sum) //
        .divide(BigDecimal.valueOf(interval), 2, RoundingMode.UP).doubleValue();
        if (interval == 5){
          this.stockOHLCs.get(i).setFiveSMA(average);
        } else if (interval == 10){
          this.stockOHLCs.get(i).setTenSMA(average);
        } else if (interval == 20){
          this.stockOHLCs.get(i).setTwentySMA(average);
        }
      }
    }
  }
}
