package com.bootcamp.demo.demo_sb_customer.controller.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.demo.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.demo.demo_sb_customer.codewave.Syscode;
import com.bootcamp.demo.demo_sb_customer.controller.UserOperation;
import com.bootcamp.demo.demo_sb_customer.dto.UserDTO;
import com.bootcamp.demo.demo_sb_customer.dto.mapper.UserDTOMapper;
import com.bootcamp.demo.demo_sb_customer.model.dto.UserDto;
import com.bootcamp.demo.demo_sb_customer.service.UserService;
import com.bootcamp.demo.demo_sb_customer.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class UserController implements UserOperation {
  @Autowired
  private UserService userService;

  @Autowired
  private UserDTOMapper userDTOMapper;

  @Override
  public List<UserDTO> getUsers() throws JsonProcessingException{
    // List<UserDTO> result = this.userServiceImpl.getUsers().stream() //
    //   .map(e -> new UserDTO(e.getId(), e.getName(), e.getUsername(), e.getEmail(), //
    //     new Address(
    //         e.getAddress().getStreet(), //
    //         e.getAddress().getSuite(), //
    //         e.getAddress().getCity(), //
    //         e.getAddress().getZipcode(), //
    //         new Geo(
    //             e.getAddress().getGeo().getLatitude(), 
    //             e.getAddress().getGeo().getLongitude())))
    //   ).collect(Collectors.toList());

    List<UserDTO> result = new ArrayList<>();
    for (UserDto u : this.userService.getUsers()){
      result.add(this.userDTOMapper.map(u));
    }
    // return ApiResp.<List<UserDTO>>builder().syscode(Syscode.OK).data(result).build();
    return result;
  }

  @Override
  public List<UserDto> createUserBy() throws JsonProcessingException{
    return this.userService.getUsers();
  }
}
