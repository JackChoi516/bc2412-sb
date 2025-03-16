package com.finance.project.final_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finance.project.final_project.entity.TStockPriceOHLCEntity;
import com.finance.project.final_project.entity.idClass.OHLCEntityIdClass;

@Repository
public interface TStockPriceOHLCRepository extends JpaRepository<TStockPriceOHLCEntity, OHLCEntityIdClass>{
  List<TStockPriceOHLCEntity> findByRegularMarketTimeGreaterThanEqualAndSymbol(Long regularMarketTime, String symbol);
}
