package com.finance.project.final_project.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiveMinListDTO {
    private String symbol;
    private String regularMarketTime;
    private Double regularMarketPrice;
    private Double regularMarketChangePercent;
}
