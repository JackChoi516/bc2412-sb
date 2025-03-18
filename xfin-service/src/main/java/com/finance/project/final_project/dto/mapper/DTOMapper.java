package com.finance.project.final_project.dto.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import com.finance.project.final_project.dto.StockFiveMinDTO;
import com.finance.project.final_project.dto.StockOHLCDTO;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.entity.TStockPriceOHLCEntity;

@Component
public class DTOMapper {
  public StockFiveMinDTO map(List<TStockPriceEntity> entities) {
    StockFiveMinDTO result = StockFiveMinDTO.builder() //
        .tStockPrices(new ArrayList<>()) //
        .build();
    for (TStockPriceEntity entity : entities) {
      StockFiveMinDTO.TStockPriceDTO tStockDTO = //
          StockFiveMinDTO.TStockPriceDTO.builder() //
              .type(entity.getType()) //
              .apiDateTime(entity.getApiDateTime()) //
              .marketTimeWithZone(entity.getMarketTimeWithZone()) //
              .symbol(entity.getSymbol()) //
              .regularMarketTime(entity.getRegularMarketTime()) //
              .regularMarketPrice(entity.getRegularMarketPrice()) //
              .regularMarketChangePercent(entity.getRegularMarketChangePercent()) //
              .build();
      tStockDTO.setSMAFiveMins(result.calculateSMAWithFive());
      result.getTStockPrices().add(tStockDTO);
    }
    int lastIdx = entities.size() - 1;
    result.setSymbol(entities.get(lastIdx).getSymbol());
    result.setConvertedMarketTime(
      LocalDateTime.ofInstant(Instant.ofEpochSecond(entities.get(lastIdx).getRegularMarketTime()), ZoneId.systemDefault())
      );
    result.setCurrentRegularMarketTime(entities.get(lastIdx).getRegularMarketTime());
    return result;
  }

  public List<StockOHLCDTO> mapOHLC(List<TStockPriceOHLCEntity> entities){
    List<StockOHLCDTO> result = new ArrayList<>();
    for (TStockPriceOHLCEntity e : entities){
      result.add(
        StockOHLCDTO.builder().regularMarketTime(e.getRegularMarketTime()) //
        .convertedDateTime(e.getConvertedDateTime()) //
        .symbol(e.getSymbol()) //
        .type(e.getType()) //
        .open(e.getOpen()) //
        .close(e.getClose()) //
        .high(e.getHigh()) //
        .low(e.getLow()) //
        .build()
        );
    }
    return result;
  }
}
