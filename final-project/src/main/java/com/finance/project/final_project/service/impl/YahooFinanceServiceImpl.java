package com.finance.project.final_project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.project.final_project.codewave.YahooFinanceManager;
import com.finance.project.final_project.controller.YahooFinanceOperation;
import com.finance.project.final_project.model.StockDataDto;
import com.finance.project.final_project.service.YahooFinanceService;

@Service
public class YahooFinanceServiceImpl implements YahooFinanceService{
  @Autowired
  private YahooFinanceManager yahooFinanceManager;

  @Override
  public String getStockDataDto(String symbols){
    return this.yahooFinanceManager.getStockDataDto(symbols);
  }
}
