package com.finance.project.final_project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finance.project.final_project.entity.TStockPriceEntity;

@Repository
public interface TStockPriceRepository extends JpaRepository<TStockPriceEntity, Long>{
  Optional<TStockPriceEntity> findTopBySymbolOrderByRegularMarketTimeDesc(String symbol);
}
