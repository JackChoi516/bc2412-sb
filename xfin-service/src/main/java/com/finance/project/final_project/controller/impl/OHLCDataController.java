package com.finance.project.final_project.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.finance.project.final_project.controller.OHLCDataOperation;
import com.finance.project.final_project.entity.TStockPriceOHLCEntity;
import com.finance.project.final_project.service.StockOHLCDataService;

@RestController
public class OHLCDataController implements OHLCDataOperation{
  @Autowired
  private StockOHLCDataService stockOHLCDataService;

  @Override
  public List<TStockPriceOHLCEntity> saveOHLCToDatabase(String symbol, Long period1, Long period2, String interval){
    return this.stockOHLCDataService.saveOHLCToDatabase(symbol, period1, period2, interval);
  }
}
