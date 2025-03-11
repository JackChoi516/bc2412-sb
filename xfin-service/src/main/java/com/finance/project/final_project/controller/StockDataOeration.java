package com.finance.project.final_project.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.dto.FiveMinDataDTO;
import com.finance.project.final_project.dto.FiveMinListDTO;
import com.finance.project.final_project.dto.StockListDTO;

public interface StockDataOeration {
  @GetMapping(value = "/stocklists")
  StockListDTO getStockLists() throws JsonProcessingException;

  @GetMapping(value = "/5minlist")
  List<FiveMinListDTO> getFiveMinList(@RequestParam String symbol) throws JsonProcessingException;

  @GetMapping(value = "/5mindata")
  FiveMinDataDTO getFiveMinData(@RequestParam String symbol) throws JsonProcessingException;
}
