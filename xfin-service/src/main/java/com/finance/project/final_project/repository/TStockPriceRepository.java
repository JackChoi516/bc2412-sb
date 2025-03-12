package com.finance.project.final_project.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finance.project.final_project.entity.TStockPriceEntity;

@Repository
public interface TStockPriceRepository extends JpaRepository<TStockPriceEntity, Long>{
  Optional<TStockPriceEntity> findTopBySymbolOrderByRegularMarketTimeDesc(String symbol);

  @Query("SELECT sp FROM TStockPriceEntity sp WHERE FUNCTION('DATE', sp.marketTimeWithZone) = :date AND sp.symbol = :symbol")
  List<TStockPriceEntity> findByDateAndSymbol(@Param("date") LocalDate date, @Param("symbol") String symbol);
}
