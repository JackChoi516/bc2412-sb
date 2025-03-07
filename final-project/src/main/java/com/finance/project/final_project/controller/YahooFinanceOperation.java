package com.finance.project.final_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.finance.project.final_project.model.StockDataDto;

public interface YahooFinanceOperation {
  @GetMapping(value = "/finance")
  StockDataDto getStockDataDto(@RequestParam String symbols) throws Exception;

  @GetMapping(value = "/crumb")
  String getCrumb() throws Exception;

}
