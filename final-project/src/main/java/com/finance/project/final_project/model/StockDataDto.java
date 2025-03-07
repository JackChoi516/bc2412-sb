package com.finance.project.final_project.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDataDto {
  private QuoteResponse quoteResponse;

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class QuoteResponse {
    private List<Result> result;
    private ErrorResp error;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
      private String language;
      private String region;
      private String quoteType;
      private String typeDisp;
      private String quoteSourceName;
      private Boolean triggerable;
      private String customPriceAlertConfidence;
      private String currency;
      private String marketState;
      private Double regularMarketChangePercent;
      private Double regularMarketPrice;
      private List<CorporateAction> corporateActions;
      private Long regularMarketTime;
      private String exchange;
      private String messageBoardId;
      private String exchangeTimezoneName;
      private String exchangeTimezoneShortName;
      private Long gmtOffSetMilliseconds;
      private String market;
      private Boolean esgPopulated;
      private Boolean hasPrePostMarketData;
      private Long firstTradeDateMilliseconds;
      private Integer priceHint;
      private Double regularMarketChange;
      private Double regularMarketDayHigh;
      private String regularMarketDayRange;
      private Double regularMarketDayLow;
      private Long regularMarketVolume;
      private Double regularMarketPreviousClose;
      private Double bid;
      private Double ask;
      private Integer bidSize;
      private Integer askSize;
      private String fullExchangeName;
      private String prevName;
      private String nameChangeDate;
      private String financialCurrency;
      private Double regularMarketOpen;
      private Long averageDailyVolume3Month;
      private Long averageDailyVolume10Day;
      private Double fiftyTwoWeekLowChange;
      private Double fiftyTwoWeekLowChangePercent;
      private String fiftyTwoWeekRange;
      private Double fiftyTwoWeekHighChange;
      private Double fiftyTwoWeekHighChangePercent;
      private Double fiftyTwoWeekLow;
      private Double fiftyTwoWeekHigh;
      private Double fiftyTwoWeekChangePercent;
      private Long earningsTimestamp;
      private Long earningsTimestampStart;
      private Long earningsTimestampEnd;
      private Long earningsCallTimestampStart;
      private Long earningsCallTimestampEnd;
      private Boolean isEarningsDateEstimate;
      private Double trailingAnnualDividendRate;
      private Double trailingPE;
      private Double dividendRate;
      private Double trailingAnnualDividendYield;
      private Double dividendYield;
      private Double epsTrailingTwelveMonths;
      private Double epsForward;
      private Double epsCurrentYear;
      private Double priceEpsCurrentYear;
      private Long sharesOutstanding;
      private Double bookValue;
      private Double fiftyDayAverage;
      private Double fiftyDayAverageChange;
      private Double fiftyDayAverageChangePercent;
      private Double twoHundredDayAverage;
      private Double twoHundredDayAverageChange;
      private Double twoHundredDayAverageChangePercent;
      private Long marketCap;
      private Double forwardPE;
      private Double priceToBook;
      private Integer sourceInterval;
      private Integer exchangeDataDelayedBy;
      private String averageAnalystRating;
      private Boolean tradeable;
      private Boolean cryptoTradeable;
      private String shortName;
      private String longName;
      private String symbol;

      @Getter
      public static class CorporateAction {
        private String header;
        private String message;
        private Meta meta;

        @Getter
        public static class Meta {
          private String eventType;
          private Long dateEpochMs;
          private String amount;
        }
      }
    }

    @Getter
    public static class ErrorResp{
      private String code;
      private String description;
    }
  }
}
