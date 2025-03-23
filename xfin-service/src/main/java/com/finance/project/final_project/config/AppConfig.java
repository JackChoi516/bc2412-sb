package com.finance.project.final_project.config;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finance.project.final_project.codewave.RedisManager;
import com.finance.project.final_project.codewave.YahooFinanceManager;

@Configuration
public class AppConfig {

  // YahooFinanceManager
  @Bean
  CookieStore cookieStore() {
    return new BasicCookieStore();
  }

  @Bean
  CloseableHttpClient httpClient(CookieStore cookieStore) {
    return HttpClients.custom().setDefaultCookieStore(cookieStore).build();
  }

  @Bean
  HttpComponentsClientHttpRequestFactory factory(CloseableHttpClient httpClient) {
    return new HttpComponentsClientHttpRequestFactory(httpClient);
  }

  @Bean
  RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory) {
    return new RestTemplate(factory);
  }

  @Bean
  YahooFinanceManager yahooFinanceManager(RestTemplate restTemplate, CookieStore cookieStore) {
    return new YahooFinanceManager(restTemplate, cookieStore);
  }

  // RedisManager
  @Bean
  ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Bean
  RedisStandaloneConfiguration redisStandaloneConfiguration() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName("xfinredis-ro.bxbskk.ng.0001.aps1.cache.amazonaws.com");
    config.setPort(6379);
    // config.setPassword(RedisPassword.of("<your-redis-password>")); // Uncomment
    // if needed
    return config;
  }

  @Bean
  RedisConnectionFactory redisConnectionFactory(RedisStandaloneConfiguration standaloneConfig) {
    return new LettuceConnectionFactory(standaloneConfig);
  }

  @Bean
  RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    return template;
  }

  @Bean
  RedisManager redisManager(RedisConnectionFactory factory, ObjectMapper objectMapper,
      RedisTemplate<String, String> redisTemplate) {
    return new RedisManager(factory, objectMapper, redisTemplate);
  }

}
