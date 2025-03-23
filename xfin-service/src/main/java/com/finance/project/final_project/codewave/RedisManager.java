package com.finance.project.final_project.codewave;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisManager {
  private RedisTemplate<String, String> redisTemplate;
  private ObjectMapper objectMapper;

  public RedisManager(RedisConnectionFactory factory, ObjectMapper objectMapper, RedisTemplate<String, String> redisTemplate) {
    // this.redisTemplate = new RedisTemplate<>();
    this.redisTemplate = redisTemplate;
    this.redisTemplate.setConnectionFactory(factory);
    this.redisTemplate.setKeySerializer(RedisSerializer.string());
    this.redisTemplate.setValueSerializer(RedisSerializer.json());
    this.redisTemplate.afterPropertiesSet();
    this.objectMapper = objectMapper;
  }

  public <T> T get(String key, Class<T> clazz) throws JsonProcessingException {
    String json = this.redisTemplate.opsForValue().get(key);
    if (json != null) {
      return this.objectMapper.readValue(json, clazz);
    }
    return null;
  }

  public void set(String key, Object object, Duration duration) throws JsonProcessingException {
    String serializedJson = this.objectMapper.writeValueAsString(object);
    this.redisTemplate.opsForValue().set(key, serializedJson, duration);
  }

  public void addToList(String key, Object object, Duration duration) throws JsonProcessingException {
    ListOperations<String, String> listOps = redisTemplate.opsForList();
    String serializedJson = this.objectMapper.writeValueAsString(object);
    listOps.rightPush(key, serializedJson);
  }

  public <T> List<T> getList(String key, Class<T> clazz) throws JsonProcessingException {
    List<String> jsonLists = redisTemplate.opsForList().range(key, 0, -1);
    if (jsonLists != null) {
      return jsonLists.stream()
          .map(e -> {
            try {
              String unescapedJson = e.replaceAll("\\\\", "");
              return objectMapper.readValue(unescapedJson, clazz);
            } catch (JsonProcessingException ex) {
              throw new RuntimeException("Error deserializing JSON", ex);
            }
          })
          .collect(Collectors.toList());
    }

    return null;
  }

  public void del(String key) {
    this.redisTemplate.delete(key);
  }

}
