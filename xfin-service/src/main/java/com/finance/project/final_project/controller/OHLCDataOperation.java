package com.finance.project.final_project.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.dto.StockOHLCDataDTO;
import com.finance.project.final_project.entity.TStockPriceOHLCEntity;

public interface OHLCDataOperation {
  @PostMapping(value = "/saveohlc/{symbol}")
  List<TStockPriceOHLCEntity> saveOHLCToDatabase(
    @PathVariable String symbol, @RequestParam Long period1, //
    @RequestParam Long period2, @RequestParam String interval //
    );
  
  @GetMapping(value = "/ohlc")
  StockOHLCDataDTO getStockOHLC(
    @RequestParam String interval,
    @RequestParam String period, 
    @RequestParam String symbol
    ) throws JsonProcessingException;
}
