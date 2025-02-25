package com.bootcamp.demo.demo_sb_customer.service.impl;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.bootcamp.demo.demo_sb_customer.entity.AddressEntity;
import com.bootcamp.demo.demo_sb_customer.entity.CompanyEntity;
import com.bootcamp.demo.demo_sb_customer.entity.GeoEntity;
import com.bootcamp.demo.demo_sb_customer.entity.UserEntity;
import com.bootcamp.demo.demo_sb_customer.entity.mapper.EntityMapper;
import com.bootcamp.demo.demo_sb_customer.model.dto.UserDto;
import com.bootcamp.demo.demo_sb_customer.repository.AddressRepository;
import com.bootcamp.demo.demo_sb_customer.repository.CompanyRepository;
import com.bootcamp.demo.demo_sb_customer.repository.GeoRepository;
import com.bootcamp.demo.demo_sb_customer.repository.UserRepository;
import com.bootcamp.demo.demo_sb_customer.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserServiceImpl implements UserService{
  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private GeoRepository geoRepository;

  @Autowired
  private EntityMapper entityMapper;

  @Value("${api.jsonplaceholder.domain}")
  private String domain;

  @Value("${api.jsonplaceholder.endpoints.users}")
  private String usersEndpoint;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;


  @Override
  public List<UserDto> getUsers() throws JsonProcessingException{
    ObjectMapper objectMapper = new ObjectMapper();
    // 1. Read Redis first, if found, return users
    // "[{}, {}, {}]"
    String json = this.redisTemplate.opsForValue().get("jph-users");
    // "[{}, {}, {}]" -> Java Object (Deserialization)
    if (json != null){
      UserDto[] userDtos = objectMapper.readValue(json, UserDto[].class);
      return Arrays.asList(userDtos);
    }


    // 2. if not found, call JPH
    // String url = "https://jsonplaceholder.typicode.com/users";
    String url = UriComponentsBuilder.newInstance()
      .scheme("https")
      .host(domain)
      .path(usersEndpoint)
      .build()
      .toUriString();
      System.out.println(url);

    List<UserDto> userDtos = Arrays.asList(this.restTemplate.getForObject(url, UserDto[].class));

    // Save User List to DB.
    // Entity, Repository

    // Clear DB
    //this.geoRepository.deleteAll();
    this.addressRepository.deleteAll();
    this.companyRepository.deleteAll();
    this.userRepository.deleteAll();

    // Save DB (procedure)
    userDtos.stream().forEach(e -> {
      // map
      UserEntity userEntity =this.entityMapper.map(e);
      this.userRepository.save(userEntity);

      AddressEntity addressEntity = this.entityMapper.map(e.getAddress());
      addressEntity.setUserEntity(userEntity);
      addressEntity = this.addressRepository.save(addressEntity);

      CompanyEntity companyEntity = this.entityMapper.map(e.getCompany());
      companyEntity.setUserEntity(userEntity);
      this.companyRepository.save(companyEntity);

      GeoEntity geoEntity = this.entityMapper.map(e.getAddress().getGeo());
      // geoEntity.setAddressEntity(addressEntity);
      this.geoRepository.save(geoEntity);

      // userEntity.setAddressEntity(addressEntity);
      // userEntity.setCompanyEntity(companyEntity);      
    
    });
    // 3. write the users back to Redis
    // Java Object -> "[{}, {}, {}]" (Serialization)
    String serializedJson = objectMapper.writeValueAsString(userDtos);
    this.redisTemplate.opsForValue().set("jph-users", serializedJson, Duration.ofMinutes(1));

    return userDtos;
  }

}
