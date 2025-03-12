package com.finance.project.final_project.controller.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.controller.StockDataOeration;
import com.finance.project.final_project.dto.FiveMinDataDTO;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.service.StockDataService;

@RestController
public class StockDataController implements StockDataOeration{
  @Autowired
  private StockDataService stockDataService;

  @Override
  public Map<String, List<String>> getStockLists() throws JsonProcessingException{
    return this.stockDataService.getStockLists();
  }

  @CrossOrigin
  @Override
  public List<TStockPriceEntity> getFiveMinList(String symbol) throws JsonProcessingException{
    return this.stockDataService.getFiveMinList(symbol);
  }

  @Override
  public FiveMinDataDTO getFiveMinData(String symbol) throws JsonProcessingException{
    return this.stockDataService.getFiveMinData(symbol);
  }
}
