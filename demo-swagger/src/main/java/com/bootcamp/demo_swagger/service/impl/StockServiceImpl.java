package com.bootcamp.demo_swagger.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.demo_swagger.entity.StockEntity;
import com.bootcamp.demo_swagger.repository.StockRepository;
import com.bootcamp.demo_swagger.service.StockService;

@Service
public class StockServiceImpl implements StockService{
  @Autowired
  private StockRepository stockRepository;

  @Override
  public Optional<StockEntity> findStock(String symbol){
    return this.stockRepository.findById(symbol);
  }

  @Override
  public StockEntity saveStock(StockEntity stockEntity){
    return this.stockRepository.save(stockEntity);
  }

  @Override
  public void deleteStock(String symbol){
    this.stockRepository.deleteById(symbol);
  }

  @Override
  public StockEntity updateStock(String symbol, StockEntity stockEntity){
    if (!this.stockRepository.existsById(symbol))
      throw new NoSuchElementException(symbol + " not found.");
    return this.stockRepository.save(stockEntity);
    
  }

  @Override
  public StockEntity patchStockDescription(String symbol, String description){
    Optional<StockEntity> entity = this.stockRepository.findById(symbol);
    if (!entity.isPresent())
      throw new NoSuchElementException(symbol + " not found.");
    entity.get().setDescription(description);
    return this.stockRepository.save(entity.get());
  }
}
