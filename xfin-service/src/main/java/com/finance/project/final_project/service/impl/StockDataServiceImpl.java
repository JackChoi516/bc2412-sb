package com.finance.project.final_project.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.codewave.RedisManager;
import com.finance.project.final_project.dto.StockFiveMinDTO;
import com.finance.project.final_project.dto.mapper.DTOMapper;
import com.finance.project.final_project.entity.StockListEntity;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.entity.mapper.EntityMapper;
import com.finance.project.final_project.model.QuoteDataDto;
import com.finance.project.final_project.repository.StockListRepository;
import com.finance.project.final_project.repository.TStockPriceRepository;
import com.finance.project.final_project.service.StockDataService;
import com.finance.project.final_project.service.YahooFinanceService;

@Service
public class StockDataServiceImpl implements StockDataService {
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

  @Autowired
  private DTOMapper dtoMapper;

  @Override
  public Map<String, List<String>> getStockLists() throws JsonProcessingException {
    Map<String, List<String>> result = new HashMap<>();
    String[] dataResult = this.redisManager.get("stock-lists", String[].class);
    if (dataResult != null && dataResult.length != 0) {
      result.put("stock-lists", Arrays.asList(dataResult));
      return result;
    }
    List<String> stockLists = this.stockListRepository.findAll() //
      .stream().map(e -> e.getSymbol()).collect(Collectors.toList());
    this.redisManager.set("stock-lists", stockLists.toArray(), Duration.ofDays(30));
    result.put("stock-lists", stockLists);
    return result;
  }

  @Override
  public void saveStockLists() throws JsonProcessingException{
    List<String> stockLists = this.stockListRepository.findAll() //
    .stream().map(e -> e.getSymbol()).collect(Collectors.toList());
  this.redisManager.set("stock-lists", stockLists.toArray(), Duration.ofDays(30));
  }

  @Override
  public String addStock(String symbol){
    StockListEntity newStock = StockListEntity.builder().symbol(symbol).build();
    this.stockListRepository.save(newStock);
    return newStock.getSymbol();
  }

  @Override
  public void saveQuoteData5M() throws JsonProcessingException {
    List<String> stockLists = null;
    try {
      stockLists = Arrays.asList(this.redisManager.get("stock-lists", String[].class));
    } catch (NullPointerException ex) {
      System.out.println("No stock lists in cache.");
      stockLists = this.getStockLists().get("stock-lists");
      System.out.println("Stock lists called in 5min method.");
    }
    
    if (stockLists != null) {
      for (String symbol : stockLists) {
        QuoteDataDto quote = //
            this.yahooFinanceService.getQuoteDataDto(symbol);
        // contdition for clearing Redis
        if (quote.getQuoteResponse().getResult().get(0).getMarketState().equals("PRE")) {
          this.redisManager.del("5MINLIST-" + symbol);
        }
        // get latest entity
        Long latestTime = null;
        Optional<TStockPriceEntity> latestOptional = //
            this.tStockPriceRepository.findTopBySymbolOrderByRegularMarketTimeDesc(symbol);
        if (latestOptional.isPresent()) {
          TStockPriceEntity latestEntity = latestOptional.get();
          latestTime = latestEntity.getRegularMarketTime();
        }
        
        Long quoteTimeNow = //
          quote.getQuoteResponse().getResult().get(0).getRegularMarketTime();

        if (quote.getQuoteResponse().getResult().get(0).getMarketState().equals("REGULAR")
          && quoteTimeNow > latestTime
          ) {
          TStockPriceEntity TStockPrice = this.entityMapper.map(quote);
          TStockPrice.setType("5M");
          this.tStockPriceRepository.save(TStockPrice);
          this.redisManager.addToList("5MINLIST-" + symbol, TStockPrice, Duration.ofDays(7));
        }
      }
    }
  }

  @Override
  public List<TStockPriceEntity> getFiveMinList(String symbol) throws JsonProcessingException {
    List<TStockPriceEntity> redisData = //
      this.redisManager.getList("5MINLIST-" + symbol, TStockPriceEntity.class);
    if (!redisData.isEmpty()){
      return redisData;
    }
    // Optional<TStockPriceEntity> latestEntity = this.tStockPriceRepository.findTopBySymbolOrderByRegularMarketTimeDesc(symbol);
    // Long latestTime = null;
    // if (latestEntity.isPresent()){
    //   latestTime = latestEntity.get().getRegularMarketTime();
    // }
    // List<TStockPriceEntity> databaseData = //
    //   this.tStockPriceRepository.findByDateAndSymbol(LocalDate.ofInstant(Instant.ofEpochSecond(latestTime), ZoneId.systemDefault()), symbol);
    // databaseData.stream().forEach(e -> {
    //   try {
    //     this.redisManager.addToList("5MINLIST-" + symbol, e, Duration.ofDays(7)); 
    //   } catch (Exception ex) {
    //     System.out.println("Json Error..");
    //   }
    // });
    // return databaseData;
    return new ArrayList<>();
  }


  @Override
  public StockFiveMinDTO getFiveMinData(String symbol) throws JsonProcessingException {
    List<TStockPriceEntity> entities = this.getFiveMinList(symbol);
    if (!entities.isEmpty()){
      StockFiveMinDTO result = this.dtoMapper.map(entities);
      return result;
    }
    TStockPriceEntity preStateEntity = this.entityMapper.map(this.yahooFinanceService.getQuoteDataDto(symbol));
    List<TStockPriceEntity> preState = new ArrayList<>();
    preState.add(preStateEntity);
    System.out.println("No redis now.");
    return this.dtoMapper.map(preState);
  }

  // latestTime.toLocalTime().isAfter(LocalTime.of(16,0))&&Instant.ofEpochSecond(quote
  // //
  // .getQuoteResponse().getResult().get(0).getRegularMarketTime()) //
  // .atZone(ZoneId.systemDefault()).toLocalTime().isAfter(LocalTime.of(8,59)) //
  // &&(Instant.ofEpochSecond(quote //
  // .getQuoteResponse().getResult().get(0).getRegularMarketTime()) //
  // .atZone(ZoneId.systemDefault()).isAfter(latestTime))
}
