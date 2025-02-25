package com.bootcamp.demo.demo_sb_customer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import com.bootcamp.demo.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.demo.demo_sb_customer.dto.UserDTO;
import com.bootcamp.demo.demo_sb_customer.model.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserOperation {
  // List of UserDto -> List of UserDTO
  @GetMapping(value = "/users")
  List<UserDTO> getUsers() throws JsonProcessingException;
  
  @GetMapping(value = "user")
  List<UserDto> createUserBy() throws JsonProcessingException;
}
