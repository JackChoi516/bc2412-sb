package com.finance.project.final_project.controller.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.controller.StockDataOeration;
import com.finance.project.final_project.dto.StockFiveMinDTO;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.service.StockDataService;
import com.finance.project.final_project.service.StockOHLCDataService;

@RestController
public class StockDataController implements StockDataOeration{
  @Autowired
  private StockDataService stockDataService;
  @Autowired
  private StockOHLCDataService stockOHLCDataService;

  @Override
  public Map<String, List<String>> getStockLists() throws JsonProcessingException{
    return this.stockDataService.getStockLists();
  }

  @Override
  public List<TStockPriceEntity> getFiveMinList(String symbol) throws JsonProcessingException{
    return this.stockDataService.getFiveMinList(symbol);
  }

  @Override
  public String addStock(String symbol){
    String result = this.stockDataService.addStock(symbol);
    Long startOneDWk = LocalDateTime.of(2014, 01, 04, 00, 00, 00).atZone(ZoneId.systemDefault()).toEpochSecond();
    Long startOneMo = LocalDateTime.of(2014, 01, 01, 00, 00, 00).atZone(ZoneId.systemDefault()).toEpochSecond();
    Long endOneDay = LocalDate.now().minusDays(1).atTime(23, 59).atZone(ZoneId.systemDefault()).toEpochSecond();
    Long endOneWeek = LocalDate.now().minusWeeks(1).atTime(23, 59).atZone(ZoneId.systemDefault()).toEpochSecond();
    Long endOneMonth = LocalDate.now().minusMonths(1).atTime(23, 59).atZone(ZoneId.systemDefault()).toEpochSecond();
    this.stockOHLCDataService.saveOHLCToDatabase(symbol, startOneDWk, endOneDay, "1d");
    this.stockOHLCDataService.saveOHLCToDatabase(symbol, startOneDWk, endOneWeek, "1wk");
    this.stockOHLCDataService.saveOHLCToDatabase(symbol, startOneMo, endOneMonth, "1mo");
    return result;
  }

  @CrossOrigin
  @Override
  public StockFiveMinDTO getFiveMinData(String symbol) throws JsonProcessingException{
    return this.stockDataService.getFiveMinData(symbol);
  }
}
