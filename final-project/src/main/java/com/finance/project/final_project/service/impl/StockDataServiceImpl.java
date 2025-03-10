package com.finance.project.final_project.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.codewave.RedisManager;
import com.finance.project.final_project.dto.StockListDTO;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.entity.mapper.EntityMapper;
import com.finance.project.final_project.model.QuoteDataDto;
import com.finance.project.final_project.repository.StockListRepository;
import com.finance.project.final_project.repository.TStockPriceRepository;
import com.finance.project.final_project.service.StockDataService;
import com.finance.project.final_project.service.YahooFinanceService;

@Service
public class StockDataServiceImpl implements StockDataService{
  @Autowired
  private StockListRepository stockListRepository;

  @Autowired
  private TStockPriceRepository tStockPriceRepository;

  @Autowired
  private RedisManager redisManager;

  @Autowired
  private YahooFinanceService yahooFinanceService;

  @Autowired
  private EntityMapper entityMapper;

  private String marketState = "REGULAR";

  @Override
  public StockListDTO getStockLists() throws JsonProcessingException{
    StockListDTO result = this.redisManager.get("stock-lists", StockListDTO.class);
    if (result != null){
      return result;
    }
    List<String> stockLists = this.stockListRepository.findAll() //
      .stream().map(e -> e.getSymbol()).collect(Collectors.toList());
    StockListDTO stockListDTO = StockListDTO.builder().stockLists(stockLists).build();
    this.redisManager.set("stock-lists", stockListDTO, Duration.ofMinutes(60));

    return stockListDTO;
  }

  @Override
  public void saveQuoteData5M() throws JsonProcessingException{
    List<String> stockLists = null;
    try {
      stockLists = this.redisManager.get("stock-lists", StockListDTO.class).getStockLists();
    } catch (NullPointerException e) {
      System.out.println("No stock lists.");
    }
      
    if (stockLists != null){
      for (String symbol : stockLists){
        QuoteDataDto quote = //
          this.yahooFinanceService.getQuoteDataDto(symbol);
        if (quote.getQuoteResponse().getResult().get(0).getMarketState().equals("REGULAR")){
          this.marketState = "REGULAR";
          TStockPriceEntity entity = this.entityMapper.map(quote);
          entity.setType("5M");
          entity.setSymbol(symbol);
          this.tStockPriceRepository.save(entity);
          this.redisManager.set(symbol, entity, Duration.ofDays(2));
        }else {
          
        }
      }
    }
  }



   public static void main(String[] args) {
        // Example ZonedDateTime (use any ZonedDateTime you have)
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(1741594084), ZoneId.of("Asia/Hong_Kong")); // Current date and time
        System.out.println(zonedDateTime);

        // Define the time you want to compare with (16:08)
        LocalTime targetTime = LocalTime.of(16, 8); // 16:08

        // Extract the time from ZonedDateTime
        LocalTime zonedTime = zonedDateTime.toLocalTime();

        // Compare if the time part is equal to 16:08
        if (zonedTime.isAfter( targetTime)) {
            System.out.println("The time is exactly 16:08.");
        } else {
            System.out.println("The time is not 16:08.");
        }

        // Optionally, you can display the ZonedDateTime and the comparison result
        System.out.println("Current ZonedDateTime: " + zonedDateTime);
    }
}
