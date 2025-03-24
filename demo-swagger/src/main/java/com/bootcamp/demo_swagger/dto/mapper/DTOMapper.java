package com.bootcamp.demo_swagger.dto.mapper;

import org.springframework.stereotype.Component;

import com.bootcamp.demo_swagger.dto.StockDTO;
import com.bootcamp.demo_swagger.entity.StockEntity;

@Component
public class DTOMapper {
  public StockDTO map(StockEntity entity){
    return StockDTO.builder().symbol(entity.getSymbol()).description(entity.getDescription()).build();
  }
}
