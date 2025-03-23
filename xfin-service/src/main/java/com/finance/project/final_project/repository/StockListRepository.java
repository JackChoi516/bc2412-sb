package com.finance.project.final_project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.finance.project.final_project.entity.StockListEntity;


@Repository
public interface StockListRepository extends JpaRepository<StockListEntity, Long> {
  Optional<StockListEntity> findBySymbol(String symbol);
}
