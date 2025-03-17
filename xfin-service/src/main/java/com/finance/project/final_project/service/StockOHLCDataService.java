package com.finance.project.final_project.service;

import java.util.List;

import com.finance.project.final_project.dto.OnewkOHLCDTO;
import com.finance.project.final_project.entity.TStockPriceOHLCEntity;

public interface StockOHLCDataService {
  List<TStockPriceOHLCEntity> saveOHLCToDatabase(String symbol, Long period1, Long period2, String interval);
  List<TStockPriceOHLCEntity> getByPeriodAndSymbol(String period, String symbol);
  List<OnewkOHLCDTO> get1wkByPeriodAndSymbol(String period, String symbol);
}
