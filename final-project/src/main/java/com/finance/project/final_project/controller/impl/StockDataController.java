package com.finance.project.final_project.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.controller.StockDataOeration;
import com.finance.project.final_project.dto.StockListDTO;
import com.finance.project.final_project.service.StockDataService;

@RestController
public class StockDataController implements StockDataOeration{
  @Autowired
  private StockDataService stockDataService;

  @Override
  public StockListDTO getStockLists() throws JsonProcessingException{
    return this.stockDataService.getStockLists();
  }
}
