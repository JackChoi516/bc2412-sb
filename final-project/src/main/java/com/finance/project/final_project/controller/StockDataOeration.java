package com.finance.project.final_project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.dto.StockListDTO;

public interface StockDataOeration {
  @GetMapping(value = "/stocklists")
  StockListDTO getStockLists() throws JsonProcessingException;
}
