package com.bootcamp.demo_swagger.entity.mapper;

import org.springframework.stereotype.Component;

import com.bootcamp.demo_swagger.dto.StockDTO;
import com.bootcamp.demo_swagger.entity.StockEntity;

@Component
public class EntityMapper {
  public StockEntity map(StockDTO stockDTO){
    return StockEntity.builder().symbol(stockDTO.getSymbol()).description(stockDTO.getDescription()).build();
  }
}
