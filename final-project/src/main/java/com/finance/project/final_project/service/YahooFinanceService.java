package com.finance.project.final_project.service;

import com.finance.project.final_project.model.QuoteDataDto;

public interface YahooFinanceService {
  QuoteDataDto getQuoteDataDto(String symbols);
  String getCrumb();
}
