package com.finance.project.final_project.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.project.final_project.dto.OnewkOHLCDTO;
import com.finance.project.final_project.dto.mapper.DTOMapper;
import com.finance.project.final_project.entity.TStockPriceOHLCEntity;
import com.finance.project.final_project.entity.mapper.EntityMapper;
import com.finance.project.final_project.model.OHLCDataDto;
import com.finance.project.final_project.repository.TStockPriceOHLCRepository;
import com.finance.project.final_project.service.StockOHLCDataService;
import com.finance.project.final_project.service.YahooFinanceService;

import jakarta.transaction.Transactional;

@Service
public class StockOHLCDataServiceImp implements StockOHLCDataService {

    private final DTOMapper DTOMapper;
  @Autowired
  private YahooFinanceService yahooFinanceService;
  @Autowired
  private EntityMapper entityMapper;
  @Autowired
  private TStockPriceOHLCRepository tStockPriceOHLCRepository;

    StockOHLCDataServiceImp(DTOMapper DTOMapper) {
        this.DTOMapper = DTOMapper;
    }

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
  public List<TStockPriceOHLCEntity> getByPeriodAndSymbol(String period, String symbol){
    int months = 0;
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
    }
    LocalDate dateNow = LocalDate.now();
    LocalDate monthsBefore = dateNow.minusMonths(months);
    Long startOfThatDay = monthsBefore.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    return this.tStockPriceOHLCRepository //
    .findByRegularMarketTimeGreaterThanEqualAndSymbol(startOfThatDay, symbol);
  }

  @Override
  public List<OnewkOHLCDTO> get1wkByPeriodAndSymbol(String period, String symbol){
    int yearFrom = 0;
    int monthFrom = 0;
    Calendar calendar = Calendar.getInstance();
    LocalDate dateNow = LocalDate.now(ZoneId.systemDefault());
    // LocalDateTime firstMonOfMonth = 
    Long startFrom = 0L;
    if (period.equals("6M")){
      monthFrom = dateNow.minusMonths(6).getMonthValue();
      yearFrom = dateNow.minusMonths(6).getYear();
    } else if (period.equals("1Y")){
      monthFrom = dateNow.minusMonths(12).getMonthValue();
      yearFrom = dateNow.minusMonths(12).getYear();
    } else if (period.equals("5Y")){
      monthFrom = dateNow.minusMonths(60).getMonthValue();
      yearFrom = dateNow.minusMonths(60).getYear();
    }
    List<TStockPriceOHLCEntity> entities = //
      this.tStockPriceOHLCRepository.findByRegularMarketTimeGreaterThanEqualAndSymbol(startFrom, symbol);

    List<OnewkOHLCDTO> result = new ArrayList<>();


    return null;
  } 
}
