package com.finance.project.final_project.config;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.codewave.RedisManager;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.entity.TStockPriceOHLCEntity;
import com.finance.project.final_project.entity.mapper.EntityMapper;
import com.finance.project.final_project.repository.TStockPriceOHLCRepository;
import com.finance.project.final_project.repository.TStockPriceRepository;
import com.finance.project.final_project.service.StockDataService;
import com.finance.project.final_project.service.StockOHLCDataService;

@Component
public class ScheduleConfig {
  @Autowired
  private StockDataService stockDataService;

  @Autowired
  private StockOHLCDataService stockOHLCDataService;

  @Autowired
  private EntityMapper entityMapper;

  @Autowired
  private TStockPriceRepository tStockPriceRepository;
  @Autowired
  private TStockPriceOHLCRepository tStockPriceOHLCRepository;

  @Scheduled(cron = "0 00 7 * * ?")
  public void saveStockLists() throws JsonProcessingException {
    this.stockDataService.saveStockLists();
    System.out.println("Stock lists saved into Redis.");
  }

  // @Scheduled(fixedRate = 300000)
  // public void saveQuoteData() throws JsonProcessingException{
  // this.stockDataService.saveQuoteData5M();
  // System.out.println("QuoteData saved. type: 5M");
  // }

  @Scheduled(fixedRate = 420000)
  public void saveOneDayRedis() throws JsonProcessingException{
    List<String> lists = this.stockDataService.getStockLists().get("stock-lists");
    for (String symbol : lists){
      this.stockOHLCDataService.saveOneDayToRedis(symbol);
    }
    System.out.println("One day OHLC saved in Redis.");
  }

  @Scheduled(fixedRate = 420000)
  public void saveOneWkRedis() throws JsonProcessingException{
    List<String> lists = this.stockDataService.getStockLists().get("stock-lists");
    for (String symbol : lists){
      this.stockOHLCDataService.saveOneWkToRedis(symbol);
    }
    System.out.println("One week OHLC saved in Redis.");
  }


  // save OHLC to database
  @Scheduled(cron = "0 00 17 * * MON-FRI")
  public void saveOneDayOHLC() throws JsonProcessingException {
    List<String> lists = this.stockDataService.getStockLists().get("stock-lists");
    for (String symbol : lists) {
      List<TStockPriceEntity> entities = tStockPriceRepository.findByDateAndSymbol(LocalDate.now(ZoneId.systemDefault()), symbol);
      if (!entities.isEmpty()){
        TStockPriceOHLCEntity result = this.entityMapper.mapOneDay(entities);
        this.tStockPriceOHLCRepository.save(result);
        System.out.println("One day saved: " + symbol);
      } else {
        System.out.println("TStock_prices today for " + symbol);
      }
    }
    System.out.println("All one day OHLC saved.");
  }

  @Scheduled(cron = "0 10 17 ? * FRI")
  public void saveOneWkOHLC() throws JsonProcessingException{
    LocalDate dateNow = LocalDate.now(ZoneId.systemDefault());
    Long startFrom = dateNow.minusDays(7).atTime(0, 0).atZone(ZoneId.systemDefault()).toEpochSecond();
    List<String> lists = this.stockDataService.getStockLists().get("stock-lists");
    for (String symbol : lists){
    List<TStockPriceOHLCEntity> entities = //
      this.tStockPriceOHLCRepository //
      .findByRegularMarketTimeGreaterThanEqualAndSymbolAndType(startFrom, symbol, "1d");
    if (!entities.isEmpty()){
    TStockPriceOHLCEntity result = this.entityMapper.mapOneWk(entities);
    this.tStockPriceOHLCRepository.save(result);
    System.out.println("One week OHLC saved: " + symbol);
    } else {
      System.out.println("No 1d OHLC for " + symbol);
    }
    }
    System.out.println("All one week OHLC saved.");
  }

  @Scheduled(cron = "0 20 17 1 * ?")
  public void saveOneMoOHLC() throws JsonProcessingException{
    LocalDate dateNow = LocalDate.now(ZoneId.systemDefault());
    Long startFrom = dateNow.minusMonths(1).atTime(0, 0).atZone(ZoneId.systemDefault()).toEpochSecond();
    List<String> lists = this.stockDataService.getStockLists().get("stock-lists");
    for (String symbol : lists){
    List<TStockPriceOHLCEntity> entities = //
      this.tStockPriceOHLCRepository //
      .findByRegularMarketTimeGreaterThanEqualAndSymbolAndType(startFrom, symbol, "1d");
      if (!entities.isEmpty()){
        TStockPriceOHLCEntity result = this.entityMapper.mapOneMo(entities);
        this.tStockPriceOHLCRepository.save(result);
        System.out.println("One month OHLC saved: " + symbol);
      } else {
        System.out.println("No 1d OHLC for " + symbol);
      }
    }
    System.out.println("All one month OHLC saved.");
  }
}
