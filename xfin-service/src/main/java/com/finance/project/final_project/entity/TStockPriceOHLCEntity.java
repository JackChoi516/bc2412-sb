package com.finance.project.final_project.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finance.project.final_project.entity.idClass.OHLCEntityIdClass;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TStock_price_OHLCs")
@IdClass(OHLCEntityIdClass.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TStockPriceOHLCEntity {
  @Id
  private Long regularMarketTime;
  @Id
  private String type;
  @Id
  private String symbol;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime convertedDateTime;
  private Double high;
  private Double low;
  private Double close;
  private Double open;
  
}
