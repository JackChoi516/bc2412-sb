package com.bootcamp.demo_swagger.service;

import java.util.Optional;

import com.bootcamp.demo_swagger.dto.StockDTO;
import com.bootcamp.demo_swagger.entity.StockEntity;

public interface StockService {
  Optional<StockEntity> findStock(String symbol);
  StockEntity saveStock(StockEntity stockEntity);
  void deleteStock(String symbol);
  StockEntity updateStock(String symbol, StockEntity stockEntity);
  StockEntity patchStockDescription(String symbol, String description);
  
}
