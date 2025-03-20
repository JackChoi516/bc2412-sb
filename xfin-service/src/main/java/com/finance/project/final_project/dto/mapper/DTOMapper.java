package com.finance.project.final_project.dto.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import com.finance.project.final_project.dto.StockFiveMinDTO;
import com.finance.project.final_project.dto.StockOHLCDataDTO;
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

  public List<StockOHLCDataDTO.StockOHLCDTO> mapOHLC(List<TStockPriceOHLCEntity> entities){
    List<StockOHLCDataDTO.StockOHLCDTO> result = new ArrayList<>();
    for (TStockPriceOHLCEntity e : entities){
      result.add(
        StockOHLCDataDTO.StockOHLCDTO.builder().regularMarketTime(e.getRegularMarketTime()) //
        .convertedDate(e.getConvertedDateTime().toLocalDate()) //
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

  public StockOHLCDataDTO.StockOHLCDTO mapOneDayOHLC(List<TStockPriceEntity> entities){
    StockOHLCDataDTO.StockOHLCDTO result = StockOHLCDataDTO.StockOHLCDTO.builder() //
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

  public StockOHLCDataDTO.StockOHLCDTO mapOneWkOHLC(List<TStockPriceEntity> entities){
    StockOHLCDataDTO.StockOHLCDTO result = StockOHLCDataDTO.StockOHLCDTO.builder() //
      .regularMarketTime(entities.get(entities.size() - 1).getRegularMarketTime()) //
      .convertedDate(entities.get(entities.size() - 1).getMarketTimeWithZone().toLocalDate()) //
      .symbol(entities.get(0).getSymbol()) //
      .type("1wk") //
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

  public StockOHLCDataDTO mapToOHLCData(List<StockOHLCDataDTO.StockOHLCDTO> dtos){
    StockOHLCDataDTO result = StockOHLCDataDTO.builder() //
      .regularMarketTime(dtos.get(dtos.size() - 1).getRegularMarketTime()) //
      .convertedDateTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(dtos.get(dtos.size() - 1).getRegularMarketTime()), ZoneId.systemDefault())) //
      .symbol(dtos.get(0).getSymbol()) //
      .type(dtos.get(0).getType()) //
      .stockOHLCs(dtos).build();
      result.calFiveSMA();
      result.calTenSMA();
      result.calTwentySMA();
    return result;
  }
}
