package com.finance.project.final_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.finance.project.final_project.entity.StockListEntity;
import com.finance.project.final_project.repository.StockListRepository;

@Component
public class PreServerStartConfig implements CommandLineRunner{
  @Autowired
  private StockListRepository stockListRepository;
  
  @Override
  public void run(String... args) throws Exception{
    if (this.stockListRepository.count() ==0 ){
      this.stockListRepository.save(StockListEntity.builder().symbol("0388.HK").build());
      this.stockListRepository.save(StockListEntity.builder().symbol("0700.HK").build());
      this.stockListRepository.save(StockListEntity.builder().symbol("0005.HK").build());
      this.stockListRepository.save(StockListEntity.builder().symbol("NVDA").build());
      this.stockListRepository.save(StockListEntity.builder().symbol("MSFT").build());
    }
    System.out.println("Stock Lists saved");
  }
}
