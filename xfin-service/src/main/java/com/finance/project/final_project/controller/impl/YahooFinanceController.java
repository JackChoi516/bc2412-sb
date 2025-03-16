package com.finance.project.final_project.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.finance.project.final_project.controller.YahooFinanceOperation;
import com.finance.project.final_project.model.QuoteDataDto;
import com.finance.project.final_project.service.YahooFinanceService;

@RestController
public class YahooFinanceController implements YahooFinanceOperation{
  @Autowired
  private YahooFinanceService yahooFinanceService;

  @Override
  public QuoteDataDto getQuoteDataDto(String symbols){
    return this.yahooFinanceService.getQuoteDataDto(symbols);
  }

  @Override
  public String getCrumb(){
    return this.yahooFinanceService.getCrumb();
  }

}
