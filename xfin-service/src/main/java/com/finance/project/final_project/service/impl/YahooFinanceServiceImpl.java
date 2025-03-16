package com.finance.project.final_project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.project.final_project.codewave.YahooFinanceManager;
import com.finance.project.final_project.dto.mapper.DTOMapper;
import com.finance.project.final_project.model.OHLCDataDto;
import com.finance.project.final_project.model.QuoteDataDto;
import com.finance.project.final_project.service.YahooFinanceService;

@Service
public class YahooFinanceServiceImpl implements YahooFinanceService{
  @Autowired
  private YahooFinanceManager yahooFinanceManager;

  @Override
  public QuoteDataDto getQuoteDataDto(String symbols){
    return this.yahooFinanceManager.getQuoteDataDto(symbols);
  }

  @Override
  public OHLCDataDto getOhlcDataDto(String symbol, Long period1, Long period2, String interval){
    return this.yahooFinanceManager.getOHLCDataDto(symbol, period1, period2, interval);
  }

  @Override
  public String getCrumb(){
    return this.yahooFinanceManager.getCrumb();
  }
}
