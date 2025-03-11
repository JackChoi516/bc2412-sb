package com.finance.project.final_project.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import com.finance.project.final_project.dto.FiveMinDataDTO;
import com.finance.project.final_project.dto.FiveMinListDTO;
import com.finance.project.final_project.dto.StockListDTO;
import com.finance.project.final_project.dto.mapper.DTOMapper;
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

  @Autowired
  private DTOMapper dtoMapper;

  @Override
  public StockListDTO getStockLists() throws JsonProcessingException{
    StockListDTO result = this.redisManager.get("stock-lists", StockListDTO.class);
    if (result != null){
      return result;
    }
    List<String> stockLists = this.stockListRepository.findAll() //
      .stream().map(e -> e.getSymbol()).collect(Collectors.toList());
    StockListDTO stockListDTO = StockListDTO.builder().stockLists(stockLists).build();
    this.redisManager.set("stock-lists", stockListDTO, Duration.ofDays(1));

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
        // get latest time and return optional
        ZonedDateTime latestTime = null;
        Optional<TStockPriceEntity> latestOptional = //
          this.tStockPriceRepository.findTopBySymbolOrderByRegularMarketTimeDesc(symbol);
        if (latestOptional.isPresent()){
          TStockPriceEntity latestEntity = latestOptional.get();
          latestTime = latestEntity.getRegularMarketTime();
        }
        // contdition for clearing Redis
        if (latestTime.toLocalTime().isAfter(LocalTime.of(16, 0)) 
            && Instant.ofEpochSecond(quote //
            .getQuoteResponse().getResult().get(0).getRegularMarketTime()) //
            .atZone(ZoneId.systemDefault()).toLocalTime().isAfter(LocalTime.of(8, 59)) //
            && (Instant.ofEpochSecond(quote //
              .getQuoteResponse().getResult().get(0).getRegularMarketTime()) //
              .atZone(ZoneId.systemDefault()).isAfter(latestTime))
            ){
              this.redisManager.del("5MINLIST-" + symbol);
        }
        if (quote.getQuoteResponse().getResult().get(0).getMarketState().equals("REGULAR")){
          TStockPriceEntity TStockPrice = this.entityMapper.map(quote);
          TStockPrice.setSymbol(symbol);
          TStockPrice.setType("5M");
          this.tStockPriceRepository.save(TStockPrice);
          this.redisManager.addToList("5MINLIST-" + symbol, this.dtoMapper.map(quote), Duration.ofDays(30));
        }
      }
    }
  }

  @Override
  public FiveMinDataDTO getFiveMinData(String symbol) throws JsonProcessingException{
    FiveMinDataDTO redisData = this.redisManager.get("5MIN-" + symbol, FiveMinDataDTO.class);
    if (redisData != null){
      return redisData;
    }
    Optional<TStockPriceEntity> latestEntity = this.tStockPriceRepository.findTopBySymbolOrderByRegularMarketTimeDesc(symbol);
    ZonedDateTime latestTime = null;
    if (latestEntity.isPresent()){
      latestTime = latestEntity.get().getRegularMarketTime();
    }

    List<TStockPriceEntity> fiveMinEntities = //
      this.tStockPriceRepository.findByDateAndSymbol(latestTime.toLocalDate(), symbol);
    
    List<FiveMinDataDTO.TimeAndData.QuoteData> datas = //
      fiveMinEntities.stream().map(e -> this.dtoMapper.map(e)).collect(Collectors.toList());
  
    FiveMinDataDTO.TimeAndData timeAndData = //
      FiveMinDataDTO.TimeAndData.builder().regularMarketTime(latestTime.toString()).data(datas).build();

    Map<String, FiveMinDataDTO.TimeAndData> dataRetrieved = new HashMap<>();
    dataRetrieved.put("5MIN-" + symbol, timeAndData);

    FiveMinDataDTO result = FiveMinDataDTO.builder().dataRetrieved(dataRetrieved).build();
    this.redisManager.set("5MIN-" + symbol, result, Duration.ofHours(12));

    return result;
  }

  @Override
  public List<FiveMinListDTO> getFiveMinList(String symbol) throws JsonProcessingException{
    // this.redisManager.set("test-" + "0700.HK", new FiveMinDataDTO(), Duration.ofSeconds(30));
    return this.redisManager.getList("5MINLIST-" + symbol, FiveMinListDTO.class);
  }

}
