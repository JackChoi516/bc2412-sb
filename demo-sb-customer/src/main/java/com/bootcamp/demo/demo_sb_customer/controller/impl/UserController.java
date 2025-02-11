package com.bootcamp.demo.demo_sb_customer.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.demo.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.demo.demo_sb_customer.codewave.Syscode;
import com.bootcamp.demo.demo_sb_customer.controller.UserOperation;
import com.bootcamp.demo.demo_sb_customer.model.User;
import com.bootcamp.demo.demo_sb_customer.service.impl.UserServiceImpl;

@RestController
public class UserController implements UserOperation {
  @Autowired
  private UserServiceImpl userServiceImpl;

  @Override
  public ApiResp<List<User>> getUsers(){
    List<User> result = this.userServiceImpl.getUsers();
    return ApiResp.<List<User>>builder().syscode(Syscode.OK).data(result).build();
  }
}
