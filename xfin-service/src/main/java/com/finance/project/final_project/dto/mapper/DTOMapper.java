package com.finance.project.final_project.dto.mapper;

import org.springframework.stereotype.Component;
import com.finance.project.final_project.dto.FiveMinDataDTO;
import com.finance.project.final_project.entity.TStockPriceEntity;

@Component
public class DTOMapper {
  public FiveMinDataDTO.TimeAndData.TStockPriceDTO map(TStockPriceEntity entity){
    FiveMinDataDTO.TimeAndData.TStockPriceDTO data = //
      FiveMinDataDTO.TimeAndData.TStockPriceDTO.builder() //
      .symbol(entity.getSymbol()) //
      .regularMarketTime(entity.getRegularMarketTime()) //
      .regularMarketPrice(entity.getRegularMarketPrice()) //
      .regularMarketChangePercent(entity.getRegularMarketChangePercent()) //
      .build();
    return data;
  }
}
