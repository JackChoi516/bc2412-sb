package com.bootcamp.demo.demo_sb_customer.entity.mapper;

import org.springframework.stereotype.Component;

import com.bootcamp.demo.demo_sb_customer.entity.AddressEntity;
import com.bootcamp.demo.demo_sb_customer.entity.CompanyEntity;
import com.bootcamp.demo.demo_sb_customer.entity.GeoEntity;
import com.bootcamp.demo.demo_sb_customer.entity.UserEntity;
import com.bootcamp.demo.demo_sb_customer.model.dto.UserDto;

@Component
public class EntityMapper {
  
  public UserEntity map(UserDto dto){
    return UserEntity.builder() //
      .email(dto.getEmail())
      .name(dto.getName())
      .username(dto.getUsername())
      .website(dto.getWebsite())
      .phone(dto.getPhone())
      .build();
  }

  public AddressEntity map(UserDto.Address address){
    return AddressEntity.builder()
      .street(address.getStreet())
      .suite(address.getSuite())
      .city(address.getCity())
      .zipcode(address.getZipcode())
      .build();
  }

  public CompanyEntity map(UserDto.Company company){
    return CompanyEntity.builder()
      .name(company.getName())
      .catchPhrase(company.getCatchPhrase())
      .bs(company.getBs())
      .build();
  }

  public GeoEntity map(UserDto.Geo geo){
    return GeoEntity.builder()
      .latitude(Double.parseDouble(geo.getLatitude()))
      .longitude(Double.valueOf(geo.getLongitude()))
      .build();
  }
}
