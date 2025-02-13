package com.bootcamp.demo.demo_sb_restful.controller.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import com.bootcamp.demo.demo_sb_restful.DemoSbRestfulApplication;

@RestController
public class BeanController {
  @GetExchange(value = "/beans")
  public List<String> getBeans(){
    return Arrays.asList(DemoSbRestfulApplication.context.getBeanDefinitionNames());
  }
}
