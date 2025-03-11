package com.finance.project.final_project.entity.mapper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.model.QuoteDataDto;

@Component
public class EntityMapper {

  public TStockPriceEntity map(QuoteDataDto quoteData) {
    TStockPriceEntity entity = TStockPriceEntity.builder() //
        .type("") //
        .apiDateTime(ZonedDateTime.now()) //
        .symbol("") //
        .regularMarketTime(
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

}
