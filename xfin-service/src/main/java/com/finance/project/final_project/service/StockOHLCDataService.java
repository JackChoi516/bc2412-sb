package com.finance.project.final_project.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.dto.StockOHLCDataDTO;
import com.finance.project.final_project.entity.TStockPriceOHLCEntity;

public interface StockOHLCDataService {
  List<TStockPriceOHLCEntity> saveOHLCToDatabase(String symbol, Long period1, Long period2, String interval);
  StockOHLCDataDTO getStockOHLC(String interval, String period, String symbol) throws JsonProcessingException;
  void saveOneDayToRedis(String symbol) throws JsonProcessingException;
  void saveOneWkToRedis(String symbol) throws JsonProcessingException;
}
