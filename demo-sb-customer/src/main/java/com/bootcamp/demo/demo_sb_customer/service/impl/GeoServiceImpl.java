package com.bootcamp.demo.demo_sb_customer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bootcamp.demo.demo_sb_customer.entity.GeoEntity;
import com.bootcamp.demo.demo_sb_customer.model.dto.UserDto;
import com.bootcamp.demo.demo_sb_customer.repository.GeoRepository;
import com.bootcamp.demo.demo_sb_customer.service.GeoService;

@Service
public class GeoServiceImpl implements GeoService {
  @Autowired
  private GeoRepository geoRepository;

  // @Override
  // public GeoEntity createGeo(UserDto.Geo geo){
  //   GeoEntity geoEntity = new GeoEntity();
  //   geoEntity.setLatitude(Double.parseDouble(geo.getLatitude()));
  //   geoEntity.setLongitude(Double.parseDouble(geo.getLongitude()));
  //   this.geoRepository.save(geoEntity);
  //   return geoEntity;
  // }
  
}
