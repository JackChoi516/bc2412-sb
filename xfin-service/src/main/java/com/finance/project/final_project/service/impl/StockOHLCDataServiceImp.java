package com.finance.project.final_project.service.impl;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.codewave.RedisManager;
import com.finance.project.final_project.dto.StockOHLCDTO;
import com.finance.project.final_project.dto.mapper.DTOMapper;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.entity.TStockPriceOHLCEntity;
import com.finance.project.final_project.entity.mapper.EntityMapper;
import com.finance.project.final_project.model.OHLCDataDto;
import com.finance.project.final_project.repository.TStockPriceOHLCRepository;
import com.finance.project.final_project.repository.TStockPriceRepository;
import com.finance.project.final_project.service.StockOHLCDataService;
import com.finance.project.final_project.service.YahooFinanceService;

import jakarta.transaction.Transactional;

@Service
public class StockOHLCDataServiceImp implements StockOHLCDataService {
  @Autowired
  private YahooFinanceService yahooFinanceService;
  @Autowired
  private EntityMapper entityMapper;
  @Autowired
  private TStockPriceOHLCRepository tStockPriceOHLCRepository;
  @Autowired
  private TStockPriceRepository tStockPriceRepository;
  @Autowired
  private DTOMapper dtoMapper;
  @Autowired
  private RedisManager redisManager;

  @Override
  @Transactional
  public List<TStockPriceOHLCEntity> saveOHLCToDatabase(String symbol, Long period1, Long period2, String interval){
    if (period2 > period1){
      OHLCDataDto dataDto = this.yahooFinanceService.getOhlcDataDto(symbol, period1, period2, interval);
      List<TStockPriceOHLCEntity> entities = this.entityMapper.mapToList(dataDto);
      for (TStockPriceOHLCEntity entity :entities){
        this.tStockPriceOHLCRepository.save(entity);
      }
      return entities;
    }
    return null;
  }

  @Override
  public List<StockOHLCDTO> getStockOHLC(String interval, String period, String symbol) throws JsonProcessingException{
    if (interval.equals("1d") || interval.equals("1wk")){
      StockOHLCDTO[] redisReturn = this.redisManager.get(interval + period + symbol, StockOHLCDTO[].class);
      if (redisReturn != null){
        System.out.println("Redis returned.");
        return Arrays.asList(redisReturn);
      }
    }

    return null;
  }


  public void saveOneDayToRedis(String symbol) throws JsonProcessingException{
    // StockOHLCDTO[] redisReturn = this.redisManager.get("1d" + period + symbol, StockOHLCDTO[].class);
    // if (redisReturn != null){
    //   System.out.println("Redis returned.");
    //   return Arrays.asList(redisReturn);
    // }
    int months = 0;
    List<String> periods = Arrays.asList(new String[]{"1M", "3M", "6M", "1Y", "5Y"});
    for (String period : periods){
    if (period.equals("1M")){
      months = 1;
    }else if (period.equals("3M")){
      months = 3;
    }else if (period.equals("6M")){
      months = 6;
    }else if (period.equals("1Y")){
      months = 12;
    }else if (period.equals("5Y")){
      months = 60;
    } else {
      throw new IllegalArgumentException();
    }
    LocalDate dateNow = LocalDate.now();
    LocalDate monthsBefore = dateNow.minusMonths(months);
    Long startOfThatDay = monthsBefore.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();

    List<StockOHLCDTO> result = this.dtoMapper.mapOHLC(this.tStockPriceOHLCRepository //
    .findByRegularMarketTimeGreaterThanEqualAndSymbolAndType(startOfThatDay, symbol, "1d"));

    if (this.yahooFinanceService.getQuoteDataDto(symbol).getQuoteResponse().getResult().get(0).getMarketState().equals("REGULAR")){
        List<TStockPriceEntity> currentDayEntities = this.tStockPriceRepository.findByDateAndSymbol(dateNow, symbol);
        if (!currentDayEntities.isEmpty()){
          StockOHLCDTO OHLCnow = this.dtoMapper.mapOneDayOHLC(currentDayEntities);
          result.add(OHLCnow);
        }
    }
    this.redisManager.set("1d" + period + symbol, result, Duration.ofMinutes(10));
  }
  }

  public void saveOneWkToRedis(String symbol) throws JsonProcessingException{
    // StockOHLCDTO[] redisReturn = this.redisManager.get("1wk" + period + symbol, StockOHLCDTO[].class);
    // if (redisReturn != null){
    //   System.out.println("redis returned.");
    //   return Arrays.asList(redisReturn);
    // }

    int yearFrom = 0;
    int monthFrom = 0;
    LocalDate dateNow = LocalDate.now(ZoneId.systemDefault());
    List<String> periods = Arrays.asList(new String[]{"6M", "1Y", "5Y"});
    for (String period : periods){
    if (period.equals("6M")){
      monthFrom = dateNow.minusMonths(6).getMonthValue();
      yearFrom = dateNow.minusMonths(6).getYear();
    } else if (period.equals("1Y")){
      monthFrom = dateNow.minusMonths(12).getMonthValue();
      yearFrom = dateNow.minusMonths(12).getYear();
    } else if (period.equals("5Y")){
      monthFrom = dateNow.minusMonths(60).getMonthValue();
      yearFrom = dateNow.minusMonths(60).getYear();
    } else {
      throw new IllegalArgumentException();
    }

    Long startFrom = LocalDate.of(yearFrom, monthFrom, 1) //
      .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)) //
      .atTime(00, 00).atZone(ZoneId.systemDefault()).toEpochSecond();

    List<StockOHLCDTO> result = //
      this.dtoMapper.mapOHLC(
        this.tStockPriceOHLCRepository.findByRegularMarketTimeGreaterThanEqualAndSymbolAndType(startFrom, symbol, "1wk")
        );
    
    // if now is regular time
    if (this.yahooFinanceService.getQuoteDataDto(symbol).getQuoteResponse().getResult().get(0).getMarketState().equals("REGULAR")){
      Long thisWkMon = dateNow.with(DayOfWeek.MONDAY).atTime(0, 0).atZone(ZoneId.systemDefault()).toEpochSecond();
      List<TStockPriceEntity> currentWkEntities = this.tStockPriceRepository.findByRegularMarketTimeGreaterThanEqualAndSymbol(thisWkMon, symbol);
      if (!currentWkEntities.isEmpty()){
        StockOHLCDTO OHLCnow = this.dtoMapper.mapOneWkOHLC(currentWkEntities);
        result.add(OHLCnow);
      }
    }
    this.redisManager.set("1wk" + period + symbol, result, Duration.ofMinutes(10));
  }
  } 
}
