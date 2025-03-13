package com.finance.project.final_project.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.dto.FiveMinDataDTO;
import com.finance.project.final_project.entity.TStockPriceEntity;

public interface StockDataService {
  Map<String, List<String>> getStockLists() throws JsonProcessingException;
  void saveQuoteData5M() throws JsonProcessingException;
  FiveMinDataDTO getFiveMinData(String symbol) throws JsonProcessingException;
  List<TStockPriceEntity> getFiveMinList(String symbol) throws JsonProcessingException;
  
}
