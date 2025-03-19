package com.finance.project.final_project.codewave;

import java.util.List;

import org.springframework.stereotype.Component;

import com.finance.project.final_project.dto.StockOHLCDTO;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.entity.TStockPriceOHLCEntity;

@Component
public class OHLCDataManager {
    public StockOHLCDTO getOneDayOHLC(List<TStockPriceEntity> entities){
    StockOHLCDTO result = StockOHLCDTO.builder() //
      .regularMarketTime(entities.get(entities.size() - 1).getRegularMarketTime()) //
      .convertedDate(entities.get(entities.size() - 1).getMarketTimeWithZone().toLocalDate()) //
      .symbol(entities.get(0).getSymbol()) //
      .type("1d") //
      .open(entities.get(0).getRegularMarketPrice()) //
      .close(entities.get(entities.size() - 1).getRegularMarketPrice()) //
      .build();
    Double high = Double.MIN_VALUE;
    Double low = Double.MAX_VALUE;
    for (TStockPriceEntity e : entities){
      if (e.getRegularMarketPrice() > high){
        high = e.getRegularMarketPrice();
      }
      if (e.getRegularMarketPrice() < low){
        low = e.getRegularMarketPrice();
      }
    }
    result.setHigh(high);
    result.setLow(low);
    return result;
  }

  public StockOHLCDTO getOneWkByDay(List<TStockPriceOHLCEntity> entities){
    StockOHLCDTO result = StockOHLCDTO.builder() //
      .regularMarketTime(entities.get(entities.size() - 1).getRegularMarketTime()) //
      .convertedDate(entities.get(entities.size() - 1).getConvertedDateTime().toLocalDate()) //
      .symbol(entities.get(0).getSymbol()) //
      .type("1wk") //
      .open(entities.get(0).getOpen()) //
      .close(entities.get(entities.size() - 1).getClose()) //
      .build();
    Double high = Double.MIN_VALUE;
    Double low = Double.MAX_VALUE;
    for (TStockPriceOHLCEntity e : entities){
      if (e.getHigh() > high){
        high = e.getHigh();
      }
      if (e.getLow() < low){
        low = e.getLow();
      }
    }
    result.setHigh(high);
    result.setLow(low);
    return result;
  }

  public StockOHLCDTO getOneMoByDay(List<TStockPriceOHLCEntity> entities){
    StockOHLCDTO result = StockOHLCDTO.builder() //
      .regularMarketTime(entities.get(entities.size() - 1).getRegularMarketTime()) //
      .convertedDate(entities.get(entities.size() - 1).getConvertedDateTime().toLocalDate()) //
      .symbol(entities.get(0).getSymbol()) //
      .type("1mo") //
      .open(entities.get(0).getOpen()) //
      .close(entities.get(entities.size() - 1).getClose()) //
      .build();
    Double high = Double.MIN_VALUE;
    Double low = Double.MAX_VALUE;
    for (TStockPriceOHLCEntity e : entities){
      if (e.getHigh() > high){
        high = e.getHigh();
      }
      if (e.getLow() < low){
        low = e.getLow();
      }
    }
    result.setHigh(high);
    result.setLow(low);
    return result;
  }
}
