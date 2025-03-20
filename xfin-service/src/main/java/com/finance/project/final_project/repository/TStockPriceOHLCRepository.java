package com.finance.project.final_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.finance.project.final_project.entity.TStockPriceOHLCEntity;
import com.finance.project.final_project.entity.idClass.OHLCEntityIdClass;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface TStockPriceOHLCRepository extends JpaRepository<TStockPriceOHLCEntity, OHLCEntityIdClass>{
  List<TStockPriceOHLCEntity> findByRegularMarketTimeGreaterThanEqualAndSymbolAndType(Long regularMarketTime, String symbol, String type);
      @Query(value = "SELECT * FROM TStockPriceOHLCEntity t WHERE t.regularMarketTime < :regularMarketTime AND t.symbol = :symbol ORDER BY t.regularMarketTime DESC LIMIT :limit", nativeQuery = true)
    List<TStockPriceOHLCEntity> findByRegularMarketTimeLessThanAndSymbolWithLimit(
            @Param("regularMarketTime") Long regularMarketTime, 
            @Param("symbol") String symbol, 
            @Param("limit") int limit);
}
