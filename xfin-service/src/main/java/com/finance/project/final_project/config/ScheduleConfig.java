package com.finance.project.final_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.codewave.RedisManager;
import com.finance.project.final_project.service.StockDataService;

@Component
public class ScheduleConfig {
  @Autowired
  private StockDataService stockDataService;

  @Autowired
  private RedisManager redisManager;
  
  @Scheduled(fixedRate = 300000)
  public void saveQuoteData() throws JsonProcessingException{
    this.stockDataService.saveQuoteData5M();
    System.out.println("QuoteData saved. type: 5M");
  }

  @Scheduled(cron = "0 30 8 * * ?")
  public void clearRedisCache() {
      
    System.out.println("Cache deleted.");
  }
}
