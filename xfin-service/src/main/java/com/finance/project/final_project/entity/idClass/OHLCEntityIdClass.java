package com.finance.project.final_project.entity.idClass;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OHLCEntityIdClass implements Serializable{
  private Long regularMarketTime;
  private String type;
  private String symbol;

  @Override
  public boolean equals(Object o) {
    if (this == o) 
    return true;
    if (!(o instanceof OHLCEntityIdClass)) 
    return false;
    OHLCEntityIdClass that = (OHLCEntityIdClass) o;
    return Objects.equals(this.regularMarketTime, that.regularMarketTime) && //
           Objects.equals(this.type, that.type) && //
           Objects.equals(this.symbol, that.symbol);
  }

  @Override
  public int hashCode() {
  return Objects.hash(regularMarketTime, type, symbol);
  }

}

