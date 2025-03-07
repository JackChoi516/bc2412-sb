package com.finance.project.final_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.project.final_project.codewave.YahooFinanceManager;

@Configuration
public class AppConfig {
  @Bean
  RestTemplate restTemplate(){
    return new RestTemplate();
  }

  @Bean
  ObjectMapper objectMapper(){
    return new ObjectMapper();
  }

  @Bean
  YahooFinanceManager yahooFinanceManager(RestTemplate restTemplate){
    return new YahooFinanceManager(restTemplate);
  }
}
