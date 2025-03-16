package com.finance.project.final_project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.finance.project.final_project.model.QuoteDataDto;

public interface YahooFinanceOperation {
  @GetMapping(value = "/quote")
  QuoteDataDto getQuoteDataDto(@RequestParam String symbols) throws Exception;

  @GetMapping(value = "/crumb")
  String getCrumb();

}
