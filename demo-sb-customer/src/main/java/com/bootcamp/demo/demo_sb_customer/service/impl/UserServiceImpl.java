package com.bootcamp.demo.demo_sb_customer.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.demo.demo_sb_customer.model.dto.UserDto;
import com.bootcamp.demo.demo_sb_customer.service.UserService;

@Service
public class UserServiceImpl implements UserService{
  @Autowired
  private RestTemplate restTemplate;

  @Override
  public List<UserDto> getUsers(){
    String url = "https://jsonplaceholder.typicode.com/users";
    return Arrays.asList(this.restTemplate.getForObject(url, UserDto[].class));
  }
}
