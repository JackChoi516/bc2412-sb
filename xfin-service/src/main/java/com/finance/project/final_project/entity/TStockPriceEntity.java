package com.finance.project.final_project.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TStOCK_PRICES")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TStockPriceEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Setter
  private String type;
  private ZonedDateTime apiDateTime;
  @Setter
  private String symbol;
  private Long regularMarketTime;
  private ZonedDateTime marketTimeWithZone;
  private Double regularMarketPrice;
  private Double regularMarketChangePercent;
  private Double bid;
  private Integer bidSize;
  private Double ask;
  private Integer askSize;
}
