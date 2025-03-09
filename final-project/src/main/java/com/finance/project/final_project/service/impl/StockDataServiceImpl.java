package com.finance.project.final_project.service.impl;

import java.time.Duration;
import java.time.Instant;
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
    List<String> stockLists = //
      this.redisManager.get("stock-lists", StockListDTO.class).getStockLists();
      
    if (stockLists != null){
      for (String symbol : stockLists){
        TStockPriceEntity entity = //
          this.entityMapper.map(this.yahooFinanceService.getQuoteDataDto(symbol));
        entity.setType("5M");
        entity.setSymbol(symbol);
        this.tStockPriceRepository.save(entity);
      }
    }
  }

}
