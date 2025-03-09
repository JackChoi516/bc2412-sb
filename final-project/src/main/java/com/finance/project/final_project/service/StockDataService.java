package com.finance.project.final_project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.dto.StockListDTO;

public interface StockDataService {
  StockListDTO getStockLists() throws JsonProcessingException;
  void saveQuoteData5M() throws JsonProcessingException;
}
