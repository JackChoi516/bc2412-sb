package com.bootcamp.demo.demo_sb_customer.config;

import java.math.BigDecimal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.client.RestTemplate;

@Configuration // bean
public class AppConfig {
  @Bean
  BigDecimal bigDecimal(){
    return BigDecimal.valueOf(10);
  }

  @Bean
  String tutor(){
    return "Vincent";
  }

  @Bean
  RestTemplate restTemplate(){
    return new RestTemplate();
  }

  // key, value -> <String, String>
  // Spring find the parameter dependency automatically
  @Bean
  RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory){
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(factory);
    redisTemplate.setKeySerializer(RedisSerializer.string());
    redisTemplate.setValueSerializer(RedisSerializer.json());
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

}
