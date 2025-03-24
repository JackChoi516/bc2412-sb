package com.finance.project.final_project.service.impl;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.project.final_project.codewave.BusinessException;
import com.finance.project.final_project.codewave.OHLCDataManager;
import com.finance.project.final_project.codewave.RedisManager;
import com.finance.project.final_project.codewave.Syscode;
import com.finance.project.final_project.dto.StockOHLCDataDTO;
import com.finance.project.final_project.dto.mapper.DTOMapper;
import com.finance.project.final_project.entity.StockListEntity;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.entity.TStockPriceOHLCEntity;
import com.finance.project.final_project.entity.mapper.EntityMapper;
import com.finance.project.final_project.model.OHLCDataDto;
import com.finance.project.final_project.repository.StockListRepository;
import com.finance.project.final_project.repository.TStockPriceOHLCRepository;
import com.finance.project.final_project.service.StockDataService;
import com.finance.project.final_project.service.StockOHLCDataService;
import com.finance.project.final_project.service.YahooFinanceService;

import jakarta.transaction.Transactional;

@Service
public class StockOHLCDataServiceImp implements StockOHLCDataService {
  @Autowired
  private YahooFinanceService yahooFinanceService;
  @Autowired
  private StockDataService stockDataService;
  @Autowired
  private OHLCDataManager ohlcDataManager;
  @Autowired
  private EntityMapper entityMapper;
  @Autowired
  private TStockPriceOHLCRepository tStockPriceOHLCRepository;
  @Autowired
  private StockListRepository stockListRepository;
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
    } else {
      throw BusinessException.of(Syscode.INVALID_INPUT);
    }
  }

  @Override
  public StockOHLCDataDTO getStockOHLC(String interval, String period, String symbol) throws JsonProcessingException{
    Optional<StockListEntity> entity = this.stockListRepository.findBySymbol(symbol);
    if (!entity.isPresent()){
      throw BusinessException.of(Syscode.INVALID_STOCK);
    }
    if (interval.equals("1d") || interval.equals("1wk")){
      StockOHLCDataDTO.StockOHLCDTO[] redisReturn = this.redisManager.get(interval + period + symbol, StockOHLCDataDTO.StockOHLCDTO[].class);
      if (redisReturn.length != 0){
        System.out.println("Redis returned.");
        List<StockOHLCDataDTO.StockOHLCDTO > ohlcList = Arrays.asList(redisReturn);
        return this.dtoMapper.mapToOHLCData(ohlcList);
      }
      System.out.println("No OHLC Redis.");
    }
    return null;
  }


  public void saveOneDayToRedis(String symbol) throws JsonProcessingException{
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
      throw BusinessException.of(Syscode.INVALID_INPUT);
    }
    LocalDate dateNow = LocalDate.now();
    LocalDate monthsBefore = dateNow.minusMonths(months);
    Long startOfThatDay = monthsBefore.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();

    List<StockOHLCDataDTO.StockOHLCDTO> result = this.dtoMapper.mapOHLC(this.tStockPriceOHLCRepository //
    .findByRegularMarketTimeGreaterThanEqualAndSymbolAndType(startOfThatDay, symbol, "1d"));

    if (this.yahooFinanceService.getQuoteDataDto(symbol).getQuoteResponse().getResult().get(0).getMarketState().equals("REGULAR")){
        List<TStockPriceEntity> currentDayEntities = this.stockDataService.getFiveMinList(symbol);
        if (!currentDayEntities.isEmpty()){
          StockOHLCDataDTO.StockOHLCDTO OHLCnow = this.ohlcDataManager.getOneDayOHLC(currentDayEntities);
          result.add(OHLCnow);
        }
    }
    this.redisManager.set("1d" + period + symbol, result, Duration.ofMinutes(10));
  }
  }

  public void saveOneWkToRedis(String symbol) throws JsonProcessingException{
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
      throw BusinessException.of(Syscode.INVALID_INPUT);
    }

    Long startFrom = LocalDate.of(yearFrom, monthFrom, 1) //
      .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)) //
      .atTime(00, 00).atZone(ZoneId.systemDefault()).toEpochSecond();

    List<StockOHLCDataDTO.StockOHLCDTO> result = //
      this.dtoMapper.mapOHLC(
        this.tStockPriceOHLCRepository.findByRegularMarketTimeGreaterThanEqualAndSymbolAndType(startFrom, symbol, "1wk")
        );
      Long thisWkMon = dateNow.with(DayOfWeek.MONDAY).atTime(0, 0).atZone(ZoneId.systemDefault()).toEpochSecond();
      List<TStockPriceOHLCEntity> currentWkEntities = this.tStockPriceOHLCRepository.findByRegularMarketTimeGreaterThanEqualAndSymbolAndType(thisWkMon, symbol, "1d");
      StockOHLCDataDTO.StockOHLCDTO untilNow = null;
      if (!currentWkEntities.isEmpty()){
        untilNow = this.ohlcDataManager.getOneWkByDay(currentWkEntities);
      }
    
    // if now is regular time
    if (this.yahooFinanceService.getQuoteDataDto(symbol).getQuoteResponse().getResult().get(0).getMarketState().equals("REGULAR")){
      if (!currentWkEntities.isEmpty()){
        List<TStockPriceEntity> currentDay = this.stockDataService.getFiveMinList(symbol);
        if (!currentDay.isEmpty()){
        TStockPriceOHLCEntity OHLCNow = this.entityMapper.mapOneDay(currentDay);
        currentWkEntities.add(OHLCNow);
        }
        untilNow = this.ohlcDataManager.getOneWkByDay(currentWkEntities);
      }
    }
    result.add(untilNow);
    this.redisManager.set("1wk" + period + symbol, result, Duration.ofMinutes(10));
  }
  } 

  public void saveOneMoToRedis(String symbol) throws JsonProcessingException{
    int yearFrom = 0;
    int monthFrom = 0;
    LocalDate dateNow = LocalDate.now(ZoneId.systemDefault());
    List<String> periods = Arrays.asList(new String[]{"1Y", "5Y"});
    for (String period : periods){
    if (period.equals("1Y")){
      monthFrom = dateNow.minusMonths(12).getMonthValue();
      yearFrom = dateNow.minusMonths(12).getYear();
    } else if (period.equals("5Y")){
      monthFrom = dateNow.minusMonths(60).getMonthValue();
      yearFrom = dateNow.minusMonths(60).getYear();
    } else {
      throw BusinessException.of(Syscode.INVALID_INPUT);
    }

    Long startFrom = LocalDate.of(yearFrom, monthFrom, 1) //
      .atTime(00, 00).atZone(ZoneId.systemDefault()).toEpochSecond();

    List<StockOHLCDataDTO.StockOHLCDTO> result = //
      this.dtoMapper.mapOHLC(
        this.tStockPriceOHLCRepository.findByRegularMarketTimeGreaterThanEqualAndSymbolAndType(startFrom, symbol, "1mo")
        );
    Long currentMoFirstDay = dateNow.withDayOfMonth(1).atTime(0, 0).atZone(ZoneId.systemDefault()).toEpochSecond();
    List<TStockPriceOHLCEntity> untilNows = this.tStockPriceOHLCRepository.findByRegularMarketTimeGreaterThanEqualAndSymbolAndType(currentMoFirstDay, symbol, "1d");
    StockOHLCDataDTO.StockOHLCDTO untilNowOHLC = this.ohlcDataManager.getOneMoByDay(untilNows);
    
    // if now is regular time
    if (this.yahooFinanceService.getQuoteDataDto(symbol).getQuoteResponse().getResult().get(0).getMarketState().equals("REGULAR")){
      List<TStockPriceEntity> currentDay = this.stockDataService.getFiveMinList(symbol);
      if (!currentDay.isEmpty()){
        TStockPriceOHLCEntity OHLCnow = this.entityMapper.mapOneDay(currentDay);
        untilNows.add(OHLCnow);
        untilNowOHLC = this.ohlcDataManager.getOneMoByDay(untilNows);
      }
    }
    result.add(untilNowOHLC);
    
    this.redisManager.set("1mo" + period + symbol, result, Duration.ofMinutes(10));
  }
  } 

  
}
