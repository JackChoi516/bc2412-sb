package com.finance.project.final_project.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.finance.project.final_project.codewave.YahooFinanceManager;
import com.finance.project.final_project.controller.impl.StockDataController;
import com.finance.project.final_project.entity.StockListEntity;
import com.finance.project.final_project.repository.StockListRepository;
import com.finance.project.final_project.repository.TStockPriceOHLCRepository;
import com.finance.project.final_project.service.StockDataService;
import com.finance.project.final_project.service.StockOHLCDataService;

@Component
public class PreServerStartConfig implements CommandLineRunner{
  @Autowired
  private StockListRepository stockListRepository;
  @Autowired
  private StockDataService stockDataService;
  @Autowired
  private StockOHLCDataService stockOHLCDataService;

  @Override
  public void run(String... args) throws Exception{
    if (this.stockListRepository.count() ==0 ){
      this.stockListRepository.save(StockListEntity.builder().symbol("0388.HK").build());
      this.stockListRepository.save(StockListEntity.builder().symbol("0700.HK").build());
      this.stockListRepository.save(StockListEntity.builder().symbol("0005.HK").build());
      this.stockListRepository.save(StockListEntity.builder().symbol("NVDA").build());
      this.stockListRepository.save(StockListEntity.builder().symbol("MSFT").build());
    }
    System.out.println("Stock Lists already in database.");
    List<String> stocklists = this.stockDataService.getStockLists().get("stock-lists");
    System.out.println("Stock lists called.");

    Long start = LocalDateTime.of(2025, 03, 17, 00, 00, 00).atZone(ZoneId.systemDefault()).toEpochSecond();
    Long end = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();

    for (String stock : stocklists){
      if (stock.contains(".HK")){
        this.stockOHLCDataService.saveOHLCToDatabase(stock, start, end, "1d");
        System.out.println("Saving 1d OHLC " + stock);
      }
    }
    System.out.println("Saving 1d OHLC complete.");
  }
}
