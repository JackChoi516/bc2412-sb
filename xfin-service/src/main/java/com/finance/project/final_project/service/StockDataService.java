package com.finance.project.final_project.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.dto.StockFiveMinDTO;
import com.finance.project.final_project.entity.TStockPriceEntity;

public interface StockDataService {
  void saveStockLists() throws JsonProcessingException;
  String addStock(String symbol);
  Map<String, List<String>> getStockLists() throws JsonProcessingException;
  void saveQuoteData5M() throws JsonProcessingException;
  StockFiveMinDTO getFiveMinData(String symbol) throws JsonProcessingException;
  List<TStockPriceEntity> getFiveMinList(String symbol) throws JsonProcessingException;
  void preServerSaveQuote() throws JsonProcessingException;
  
}
