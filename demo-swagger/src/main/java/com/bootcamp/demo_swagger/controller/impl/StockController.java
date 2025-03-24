package com.bootcamp.demo_swagger.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.demo_swagger.controller.StockOperation;
import com.bootcamp.demo_swagger.dto.StockDTO;
import com.bootcamp.demo_swagger.dto.mapper.DTOMapper;
import com.bootcamp.demo_swagger.entity.StockEntity;
import com.bootcamp.demo_swagger.entity.mapper.EntityMapper;
import com.bootcamp.demo_swagger.service.StockService;

@RestController
public class StockController implements StockOperation{
  @Autowired
  private StockService stockService;
  @Autowired
  private DTOMapper dtoMapper;
  @Autowired
  private EntityMapper entityMapper;

  @Override
  public StockDTO getStock(String symbol) {
    StockEntity stockEntity = this.stockService.findStock(symbol).orElse(null);
    return this.dtoMapper.map(stockEntity);
  }

  @Override
  public StockDTO createStock(StockDTO stockDTO) {
    StockEntity stockEntity = this.entityMapper.map(stockDTO);
    stockEntity = this.stockService.saveStock(stockEntity);
    return this.dtoMapper.map(stockEntity);
  }

  @Override
  public void deleteStock(String symbol) {
    this.stockService.deleteStock(symbol);
  }

  @Override
  public StockDTO updateStock(String symbol, StockDTO stockDTO) {
    StockEntity stockEntity = this.entityMapper.map(stockDTO);
    stockEntity = this.stockService.updateStock(symbol, stockEntity);
    return this.dtoMapper.map(stockEntity);
  }

  @Override
  public StockDTO patchStockDesvription(String symbol, String stockDescription) {
    StockEntity stockEntity = this.stockService.patchStockDescription(symbol, stockDescription);
    return this.dtoMapper.map(stockEntity);
  }
}
