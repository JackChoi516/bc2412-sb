package com.finance.project.final_project.service;

import com.finance.project.final_project.model.StockDataDto;

public interface YahooFinanceService {
  StockDataDto getStockDataDto(String symbols);
}
