package com.finance.project.final_project.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.finance.project.final_project.repository.TStockPriceRepository;
import org.springframework.stereotype.Component;
import com.finance.project.final_project.dto.FiveMinDataDTO;
import com.finance.project.final_project.entity.TStockPriceEntity;

@Component
public class DTOMapper {
  public FiveMinDataDTO map(List<TStockPriceEntity> entities) {
    FiveMinDataDTO result = FiveMinDataDTO.builder() //
        .tStockPrices(new ArrayList<>()) //
        .build();
    for (TStockPriceEntity entity : entities) {
      FiveMinDataDTO.TStockPriceDTO tStockDTO = //
          FiveMinDataDTO.TStockPriceDTO.builder() //
              .type(entity.getType()) //
              .apiDateTime(entity.getApiDateTime()) //
              .marketTimeWithZone(entity.getMarketTimeWithZone()) //
              .symbol(entity.getSymbol()) //
              .regularMarketTime(entity.getRegularMarketTime()) //
              .regularMarketPrice(entity.getRegularMarketPrice()) //
              .regularMarketChangePercent(entity.getRegularMarketChangePercent()) //
              .build();
      result.getTStockPrices().add(tStockDTO);
      tStockDTO.setSMAFiveMins(result.calculateSMAWithFive());
    }
    int lastIdx = entities.size() - 1;
    result.setSymbol(entities.get(lastIdx).getSymbol());
    result.setConvertedDateTime(
      entities.get(lastIdx).getMarketTimeWithZone().toLocalDateTime());
    result.setCurrentRegularMarketTime(entities.get(lastIdx).getRegularMarketTime());
    return result;
  }
}
