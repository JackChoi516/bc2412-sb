package com.finance.project.final_project.entity.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.entity.TStockPriceOHLCEntity;
import com.finance.project.final_project.model.OHLCDataDto;
import com.finance.project.final_project.model.QuoteDataDto;

@Component
public class EntityMapper {
  public TStockPriceEntity map(QuoteDataDto quoteData) {
    TStockPriceEntity entity = TStockPriceEntity.builder() //
        .type("") //
        .apiDateTime(ZonedDateTime.now()) //
        .symbol(quoteData.getQuoteResponse().getResult().get(0).getSymbol()) //
        .regularMarketTime(quoteData.getQuoteResponse().getResult().get(0).getRegularMarketTime()) //
        .marketTimeWithZone(
        ZonedDateTime.ofInstant(
          Instant.ofEpochSecond(quoteData.getQuoteResponse().getResult().get(0).getRegularMarketTime())
        , ZoneId.systemDefault())
        ) //
        .regularMarketPrice(quoteData.getQuoteResponse().getResult().get(0).getRegularMarketPrice()) //
        .regularMarketChangePercent(
            quoteData.getQuoteResponse().getResult().get(0).getRegularMarketChangePercent() //
            ) //
        .bid(quoteData.getQuoteResponse().getResult().get(0).getBid()) //
        .bidSize(quoteData.getQuoteResponse().getResult().get(0).getBidSize()) //
        .ask(quoteData.getQuoteResponse().getResult().get(0).getAsk()) //
        .askSize(quoteData.getQuoteResponse().getResult().get(0).getAskSize()) //
        .build();
    return entity;
  }

  public List<TStockPriceOHLCEntity> mapToList(OHLCDataDto dataDto){
    List<TStockPriceOHLCEntity> entities = new ArrayList<>();
    OHLCDataDto.Chart.Result resultDto = dataDto.getChart().getResult().get(0);
    OHLCDataDto.Chart.Result.Indicators.Quote quoteDto = resultDto.getIndicators().getQuote().get(0);

    List<Long> timeStamps = resultDto.getTimestamp();
    for (Long timeStamp : timeStamps){
      entities.add(
        TStockPriceOHLCEntity.builder().regularMarketTime(timeStamp) //
        .convertedDateTime(LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timeStamp), ZoneId.systemDefault())).build()
        );
    }
    for (TStockPriceOHLCEntity entity : entities){
      Double open = quoteDto.getOpen().get(entities.indexOf(entity));
      if (open != null){
        open = BigDecimal.valueOf(open).setScale(2, RoundingMode.HALF_UP).doubleValue();
      }
      entity.setOpen(open);

      Double close = quoteDto.getClose().get(entities.indexOf(entity));
      if (close != null){
        close = BigDecimal.valueOf(close).setScale(2, RoundingMode.HALF_UP).doubleValue();
      }
      entity.setClose(close);

      Double high = quoteDto.getHigh().get(entities.indexOf(entity));
      if (high != null){
        high = BigDecimal.valueOf(high).setScale(2, RoundingMode.HALF_UP).doubleValue();
      }
      entity.setHigh(high);

      Double low = quoteDto.getLow().get(entities.indexOf(entity));
      if (low != null){
        low = BigDecimal.valueOf(low).setScale(2, RoundingMode.HALF_UP).doubleValue();
      }
      entity.setLow(low);
      
      entity.setType(resultDto.getMeta().getDataGranularity());
      entity.setSymbol(resultDto.getMeta().getSymbol());
    }
    return entities;
  }

  public TStockPriceOHLCEntity mapOneDay(List<TStockPriceEntity> entities){
    Double high = Double.MIN_VALUE;
    Double low = Double.MAX_VALUE;
    TStockPriceOHLCEntity result = TStockPriceOHLCEntity.builder() //
        .regularMarketTime(entities.get(entities.size() - 1).getRegularMarketTime()) //
        .convertedDateTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(entities.get(entities.size() - 1).getRegularMarketTime()), ZoneId.systemDefault())) //
        .symbol(entities.get(0).getSymbol()) //
        .type("1d") //
        .open(entities.get(0).getRegularMarketPrice()) //
        .close(entities.get(entities.size() - 1).getRegularMarketPrice()).build();

    for (TStockPriceEntity e : entities){
      if (e.getRegularMarketPrice() > high){
        high = e.getRegularMarketPrice();
      }
      if (e.getRegularMarketPrice() < low){
        low = e.getRegularMarketPrice();
      }
    }
    result.setHigh(high);
    result.setLow(low);
    return result;
  }

  public TStockPriceOHLCEntity mapOneWk(List<TStockPriceOHLCEntity> entities){
    Double high = Double.MIN_VALUE;
    Double low = Double.MAX_VALUE;
    TStockPriceOHLCEntity result = TStockPriceOHLCEntity.builder() //
        .regularMarketTime(entities.get(entities.size() - 1).getRegularMarketTime()) //
        .convertedDateTime(entities.get(entities.size() - 1).getConvertedDateTime()) //
        .symbol(entities.get(0).getSymbol()) //
        .type("1wk") //
        .open(entities.get(0).getOpen()) //
        .close(entities.get(entities.size() - 1).getClose()).build();

    for (TStockPriceOHLCEntity e : entities){
      if (e.getHigh() > high){
        high = e.getHigh();
      }
      if (e.getLow() < low){
        low = e.getLow();
      }
    }
    result.setHigh(high);
    result.setLow(low);
    return result;
  }

  public TStockPriceOHLCEntity mapOneMo(List<TStockPriceOHLCEntity> entities){
    Double high = Double.MIN_VALUE;
    Double low = Double.MAX_VALUE;
    TStockPriceOHLCEntity result = TStockPriceOHLCEntity.builder() //
        .regularMarketTime(entities.get(entities.size() - 1).getRegularMarketTime()) //
        .convertedDateTime(entities.get(entities.size() - 1).getConvertedDateTime()) //
        .symbol(entities.get(0).getSymbol()) //
        .type("1mo") //
        .open(entities.get(0).getOpen()) //
        .close(entities.get(entities.size() - 1).getClose()).build();

    for (TStockPriceOHLCEntity e : entities){
      if (e.getHigh() > high){
        high = e.getHigh();
      }
      if (e.getLow() < low){
        low = e.getLow();
      }
    }
    result.setHigh(high);
    result.setLow(low);
    return result;
  }
}
