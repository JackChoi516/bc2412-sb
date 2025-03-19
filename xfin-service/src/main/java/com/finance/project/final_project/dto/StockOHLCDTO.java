package com.finance.project.final_project.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.entity.TStockPriceOHLCEntity;

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

  public void setOneDayOHLC(List<TStockPriceEntity> entities) {
        this.regularMarketTime = (entities.get(entities.size() - 1).getRegularMarketTime());
        this.convertedDate = (entities.get(entities.size() - 1).getMarketTimeWithZone().toLocalDate());
        this.symbol = (entities.get(0).getSymbol());
        this.type = ("1d");
        this.open = (entities.get(0).getRegularMarketPrice()); //
        this.close = (entities.get(entities.size() - 1).getRegularMarketPrice());
    Double high = Double.MIN_VALUE;
    Double low = Double.MAX_VALUE;
    for (TStockPriceEntity e : entities) {
      if (e.getRegularMarketPrice() > high) {
        high = e.getRegularMarketPrice();
      }
      if (e.getRegularMarketPrice() < low) {
        low = e.getRegularMarketPrice();
      }
    }
    this.high = high;
    this.low = low;
  }


}
