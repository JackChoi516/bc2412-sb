package com.finance.project.final_project.service;

import com.finance.project.final_project.model.OHLCDataDto;
import com.finance.project.final_project.model.QuoteDataDto;

public interface YahooFinanceService {
  QuoteDataDto getQuoteDataDto(String symbols);
  OHLCDataDto getOhlcDataDto(String symbol, Long period1, Long period2, String interval);
  String getCrumb();
}
