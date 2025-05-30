package com.finance.project.final_project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.dto.StockFiveMinDTO;
import com.finance.project.final_project.entity.TStockPriceEntity;

public interface StockDataOeration {
  @GetMapping(value = "/stocklists")
  Map<String, List<String>> getStockLists() throws JsonProcessingException;

  @GetMapping(value = "/5minlist")
  List<TStockPriceEntity> getFiveMinList(@RequestParam String symbol) throws JsonProcessingException;

  @GetMapping(value = "/5mindata")
  StockFiveMinDTO getFiveMinData(@RequestParam String symbol) throws JsonProcessingException;

  @PostMapping(value = "/addstock")
  String addStock(@RequestParam String symbol);
}
