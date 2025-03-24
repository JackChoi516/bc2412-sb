package com.bootcamp.demo_swagger.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tstocks")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockEntity implements Serializable{
  @Id
  private String symbol;
  @Setter
  private String description;
}
