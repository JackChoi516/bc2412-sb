package com.bootcamp.demo.demo_sb_customer.dto.mapper;

import org.springframework.stereotype.Component;

import com.bootcamp.demo.demo_sb_customer.dto.UserDTO;
import com.bootcamp.demo.demo_sb_customer.model.dto.UserDto;

@Component
public class UserDTOMapper {
  public UserDTO map(UserDto userDto){
    return new UserDTO(userDto.getId(), userDto.getName(), userDto.getUsername(), userDto.getEmail(), new UserDTO.Address(userDto.getAddress().getStreet(), userDto.getAddress().getSuite(), userDto.getAddress().getCity(), userDto.getAddress().getZipcode(), new UserDTO.Geo(userDto.getAddress().getGeo().getLatitude(), userDto.getAddress().getGeo().getLongitude())));
  
  }
}
