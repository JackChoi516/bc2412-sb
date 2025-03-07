package com.finance.project.final_project.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.finance.project.final_project.codewave.CrumbManager;
import com.finance.project.final_project.codewave.YahooFinanceManager;
import com.finance.project.final_project.controller.YahooFinanceOperation;
import com.finance.project.final_project.model.StockDataDto;
import com.finance.project.final_project.service.YahooFinanceService;

@RestController
public class YahooFinanceController implements YahooFinanceOperation{
  @Autowired
  private YahooFinanceService yahooFinanceService;
///
  @Autowired
  private CrumbManager crumbManager;
  @Autowired
  private YahooFinanceManager yahooFinanceManager;

  @Override
  public StockDataDto getStockDataDto(String symbols){
    return this.yahooFinanceService.getStockDataDto(symbols);
  }

  @Override
  public String getCrumb(){
    return this.yahooFinanceManager.getKey();
    // return this.crumbManager.getKey();
  }

  @Override
  public String getNewCookie(){
    return this.yahooFinanceManager.getNewCookie();
  }
}
