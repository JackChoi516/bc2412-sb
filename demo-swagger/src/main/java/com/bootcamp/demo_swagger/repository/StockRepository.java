package com.bootcamp.demo_swagger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.demo_swagger.entity.StockEntity;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, String>{
  
}
