package com.finance.project.final_project.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import com.finance.project.final_project.codewave.BusinessException;
import com.finance.project.final_project.codewave.RedisManager;
import com.finance.project.final_project.codewave.Syscode;
import com.finance.project.final_project.dto.StockFiveMinDTO;
import com.finance.project.final_project.dto.mapper.DTOMapper;
import com.finance.project.final_project.entity.StockListEntity;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.entity.mapper.EntityMapper;
import com.finance.project.final_project.model.QuoteDataDto;
import com.finance.project.final_project.repository.StockListRepository;
import com.finance.project.final_project.repository.TStockPriceRepository;
import com.finance.project.final_project.service.StockDataService;
import com.finance.project.final_project.service.StockOHLCDataService;
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
  private StockOHLCDataService stockOHLCDataService;
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
    if (stockLists.isEmpty()) {
      throw BusinessException.of(Syscode.STOCK_LISTS_NOT_FOUND);
    }
    this.redisManager.set("stock-lists", stockLists.toArray(), Duration.ofDays(30));
    result.put("stock-lists", stockLists);
    return result;
  }

  @Override
  public void saveStockLists() throws JsonProcessingException {
    List<String> stockLists = this.stockListRepository.findAll() //
        .stream().map(e -> e.getSymbol()).collect(Collectors.toList());
    if (stockLists.isEmpty()) {
      throw BusinessException.of(Syscode.STOCK_LISTS_NOT_FOUND);
    }
    this.redisManager.set("stock-lists", stockLists.toArray(), Duration.ofDays(30));
  }

  @Override
  public String addStock(String symbol) {
    StockListEntity newStock = null;
    Optional<StockListEntity> existStock = this.stockListRepository.findBySymbol(symbol);
    if (!existStock.isPresent()) {
      newStock = StockListEntity.builder().symbol(symbol).build();
      this.stockListRepository.save(newStock);
    } else {
      return "Stock already exist: " + existStock.get().getSymbol();
    }
    QuoteDataDto quoteDto = this.yahooFinanceService.getQuoteDataDto(symbol);
    if (!quoteDto.getQuoteResponse().getResult().isEmpty()){
      TStockPriceEntity priceEntity = this.entityMapper.map(quoteDto);
      priceEntity.setType("firstSave");
      this.tStockPriceRepository.save(priceEntity);

    Long startOneDWk = LocalDateTime.of(2005, 01, 04, 00, 00, 00).atZone(ZoneId.systemDefault()).toEpochSecond();
    Long startOneMo = LocalDateTime.of(2005, 01, 01, 00, 00, 00).atZone(ZoneId.systemDefault()).toEpochSecond();
    Long endOneDay = LocalDate.now().minusDays(1).atTime(23, 59).atZone(ZoneId.systemDefault()).toEpochSecond();
    Long endOneWeek = LocalDate.now().minusWeeks(1).atTime(23, 59).atZone(ZoneId.systemDefault()).toEpochSecond();
    Long endOneMonth = LocalDate.now().minusMonths(1).atTime(23, 59).atZone(ZoneId.systemDefault()).toEpochSecond();
    this.stockOHLCDataService.saveOHLCToDatabase(symbol, startOneDWk, endOneDay, "1d");
    this.stockOHLCDataService.saveOHLCToDatabase(symbol, startOneDWk, endOneWeek, "1wk");
    this.stockOHLCDataService.saveOHLCToDatabase(symbol, startOneMo, endOneMonth, "1mo");
      return newStock.getSymbol();
    } else {
      throw BusinessException.of(Syscode.INVALID_STOCK);
    }
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
            && quoteTimeNow > latestTime) {
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
    if (!redisData.isEmpty()) {
      return redisData;
    }
    return new ArrayList<>();
  }

  @Override
  public StockFiveMinDTO getFiveMinData(String symbol) throws JsonProcessingException {
    List<TStockPriceEntity> entities = this.getFiveMinList(symbol);
    if (!entities.isEmpty()) {
      StockFiveMinDTO result = this.dtoMapper.map(entities);
      return result;
    }
    TStockPriceEntity preStateEntity = this.entityMapper.map(this.yahooFinanceService.getQuoteDataDto(symbol));
    List<TStockPriceEntity> preState = new ArrayList<>();
    preState.add(preStateEntity);
    System.out.println("No redis now for " + symbol);
    return this.dtoMapper.map(preState);
  }

  @Override
  public void preServerSaveQuote() throws JsonProcessingException {
    List<String> lists = this.getStockLists().get("stock-lists");
    if (!lists.isEmpty()) {
      for (String symbol : lists) {
        QuoteDataDto quoteDto = this.yahooFinanceService.getQuoteDataDto(symbol);
        TStockPriceEntity priceEntity = this.entityMapper.map(quoteDto);
        priceEntity.setType("startServer");
        this.tStockPriceRepository.save(priceEntity);
      }
    }
  }
  // latestTime.toLocalTime().isAfter(LocalTime.of(16,0))&&Instant.ofEpochSecond(quote
  // //
  // .getQuoteResponse().getResult().get(0).getRegularMarketTime()) //
  // .atZone(ZoneId.systemDefault()).toLocalTime().isAfter(LocalTime.of(8,59)) //
  // &&(Instant.ofEpochSecond(quote //
  // .getQuoteResponse().getResult().get(0).getRegularMarketTime()) //
  // .atZone(ZoneId.systemDefault()).isAfter(latestTime))
}
