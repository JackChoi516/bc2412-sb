package com.bootcamp.demo.demo_sb_customer.codewave;

import java.time.Duration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisManager {
  private static final Duration DEFAULT_DURATION = Duration.ofMinutes(1);

  private RedisTemplate<String, String> redisTemplate;
  private ObjectMapper objectMapper;

  public RedisManager(RedisConnectionFactory factory, ObjectMapper objectMapper){
    this.redisTemplate = new RedisTemplate<>();
    this.redisTemplate.setConnectionFactory(factory);
    this.redisTemplate.setKeySerializer(RedisSerializer.string());
    this.redisTemplate.setValueSerializer(RedisSerializer.json());
    this.redisTemplate.afterPropertiesSet();
    this.objectMapper = objectMapper;
  }

  // UserDto userDto = new RestTemplate.getForObject(url, UserDto.class)
  public <T> T get(String key, Class<T> clazz) throws JsonProcessingException{
    String json = this.redisTemplate.opsForValue().get(key);
    if (json != null){
      return this.objectMapper.readValue(json, clazz);
    }
    return null;
  }

  public void set(String key, Object object) throws JsonProcessingException{
    String serializedJson = objectMapper.writeValueAsString(object);
    this.redisTemplate.opsForValue().set(key, serializedJson, DEFAULT_DURATION);
  }
}
