package com.finance.project.final_project.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.dto.FiveMinDataDTO;
import com.finance.project.final_project.dto.FiveMinListDTO;
import com.finance.project.final_project.dto.StockListDTO;

public interface StockDataService {
  StockListDTO getStockLists() throws JsonProcessingException;
  void saveQuoteData5M() throws JsonProcessingException;
  FiveMinDataDTO getFiveMinData(String symbol) throws JsonProcessingException;
  List<FiveMinListDTO> getFiveMinList(String symbol) throws JsonProcessingException;
}
