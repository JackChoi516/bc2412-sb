package com.bootcamp.demo.demo_sb_customer.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
  private AddressServiceImpl addressServiceImpl;

  @Autowired
  private CompanyServiceImpl companyServiceImpl;

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



  @Override
  public List<UserDto> getUsers(){
    String url = "https://jsonplaceholder.typicode.com/users";
    List<UserDto> userDtos = Arrays.asList(this.restTemplate.getForObject(url, UserDto[].class));

    // Save User List to DB.
    // Entity, Repository
    userDtos.stream().forEach(e -> {
      // map
      UserEntity userEntity = this.entityMapper.map(e);

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
      this.userRepository.save(userEntity);
      //this.createUser(e);
    
    });
    return userDtos;

  }

  // @Override
  // public UserEntity createUser(UserDto userDto){
  //   UserEntity userEntity = new UserEntity();
  //   userEntity.setName(userDto.getName());
  //   userEntity.setUsername(userDto.getUsername());
  //   userEntity.setEmail(userDto.getEmail());
  //   userEntity.setPhone(userDto.getPhone());
  //   userEntity.setWebsite(userDto.getWebsite());
  //   userEntity = this.userRepository.save(userEntity);

  //   userEntity.setAddressEntity(this.addressServiceImpl.createAddress(userDto.getAddress()), userEntity);
  //   userEntity.setCompanyEntity(this.companyServiceImpl.createCompany(userDto.getCompany()));
   
  //   return userEntity;
    
  // }
}
