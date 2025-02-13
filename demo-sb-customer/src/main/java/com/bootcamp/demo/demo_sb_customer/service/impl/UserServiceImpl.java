package com.bootcamp.demo.demo_sb_customer.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


  @Override
  public List<UserDto> getUsers(){
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
    this.geoRepository.deleteAll();
    this.addressRepository.deleteAll();
    this.companyRepository.deleteAll();
    this.userRepository.deleteAll();

    // Save DB (procedure)
    userDtos.stream().forEach(e -> {
      // map
      UserEntity userEntity = this.userRepository.save(this.entityMapper.map(e));

      AddressEntity addressEntity = this.entityMapper.map(e.getAddress());
      addressEntity.setUserEntity(userEntity);
      this.addressRepository.save(addressEntity);

      GeoEntity geoEntity = this.entityMapper.map(e.getAddress().getGeo());
      geoEntity.setAddressEntity(addressEntity);
      this.geoRepository.save(geoEntity);

      CompanyEntity companyEntity = this.entityMapper.map(e.getCompany());
      companyEntity.setUserEntity(userEntity);
      this.companyRepository.save(companyEntity);
      
      userEntity.setAddressEntity(addressEntity);
      userEntity.setCompanyEntity(companyEntity);
      
    
    });
    return userDtos;
  }

}
