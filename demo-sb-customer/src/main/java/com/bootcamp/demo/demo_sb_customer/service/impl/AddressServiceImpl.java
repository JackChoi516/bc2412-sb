package com.bootcamp.demo.demo_sb_customer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.demo.demo_sb_customer.entity.AddressEntity;
import com.bootcamp.demo.demo_sb_customer.entity.UserEntity;
import com.bootcamp.demo.demo_sb_customer.model.dto.UserDto;
import com.bootcamp.demo.demo_sb_customer.repository.AddressRepository;
import com.bootcamp.demo.demo_sb_customer.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService{
  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private GeoServiceImpl geoServiceImpl;

  // @Override
  // public AddressEntity createAddress(UserDto.Address address, UserEntity userEntity){
  //   AddressEntity addressEntity = new AddressEntity();
  //   addressEntity.setStreet(address.getStreet());
  //   addressEntity.setSuite(address.getSuite());
  //   addressEntity.setCity(address.getCity());
  //   addressEntity.setZipcode(address.getZipcode());
  //   addressEntity.setGeoEntity(this.geoServiceImpl.createGeo(address.getGeo()));
  //   addressEntity.setUserEntity(userEntity);
  //   this.addressRepository.save(addressEntity);
  //   return addressEntity;
  // }
  
}
