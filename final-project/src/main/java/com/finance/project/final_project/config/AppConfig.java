package com.finance.project.final_project.config;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.project.final_project.codewave.RedisManager;
import com.finance.project.final_project.codewave.YahooFinanceManager;

@Configuration
public class AppConfig {

  // YahooFinanceManager
  @Bean
  CookieStore cookieStore(){
    return new BasicCookieStore();
  } 

  @Bean
  CloseableHttpClient httpClient(CookieStore cookieStore){
    return HttpClients.custom().setDefaultCookieStore(cookieStore).build();
  }

  @Bean
  HttpComponentsClientHttpRequestFactory factory(CloseableHttpClient httpClient){
    return new HttpComponentsClientHttpRequestFactory(httpClient);
  } 
  
  @Bean
  RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory){
    return new RestTemplate(factory);
  }

  @Bean
  YahooFinanceManager yahooFinanceManager(RestTemplate restTemplate, CookieStore cookieStore){
    return new YahooFinanceManager(restTemplate, cookieStore);
  }

  // RedisManager
  @Bean
  ObjectMapper objectMapper(){
    return new ObjectMapper();
  }

  @Bean
  RedisManager redisManager(RedisConnectionFactory factory, ObjectMapper objectMapper){
    return new RedisManager(factory, objectMapper);
  }

}
