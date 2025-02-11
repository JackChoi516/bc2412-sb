package com.bootcamp.demo.demo_sb_customer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import com.bootcamp.demo.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.demo.demo_sb_customer.model.User;

public interface UserOperation {
  @GetMapping(value = "/users")
  ApiResp<List<User>> getUsers();
}
