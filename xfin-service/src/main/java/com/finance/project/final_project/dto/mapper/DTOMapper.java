package com.finance.project.final_project.dto.mapper;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import com.finance.project.final_project.dto.FiveMinDataDTO;
import com.finance.project.final_project.entity.TStockPriceEntity;
import com.finance.project.final_project.model.QuoteDataDto;

@Component
public class DTOMapper {
  // public FiveMinListDTO map(QuoteDataDto quote){
  //   return FiveMinListDTO.builder() //
  //     .symbol(quote.getQuoteResponse().getResult().get(0).getSymbol()) //
  //     .regularMarketTime(
  //        ZonedDateTime.ofInstant(
  //         Instant.ofEpochSecond(quote.getQuoteResponse().getResult().get(0).getRegularMarketTime())
  //       , ZoneId.systemDefault()).toString()
  //       ) //
  //     .regularMarketPrice(quote.getQuoteResponse().getResult().get(0).getRegularMarketPrice()) //
  //     .regularMarketChangePercent(quote.getQuoteResponse().getResult().get(0).getRegularMarketChangePercent()) //
  //     .build();
  // }

  public FiveMinDataDTO.TimeAndData.QuoteData map(TStockPriceEntity entity){
    FiveMinDataDTO.TimeAndData.QuoteData data = //
      FiveMinDataDTO.TimeAndData.QuoteData.builder() //
      .symbol(entity.getSymbol()) //
      .regularMarketTime(entity.getMarketTimeWithZone().withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))) //
      .regularMarketPrice(entity.getRegularMarketPrice()) //
      .regularMarketChangePercent(entity.getRegularMarketChangePercent()) //
      .build();
    return data;
  }
}
