package com.bootcamp.demo.goodbye.demo_sb_goodbye.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodbyeController {
  @GetMapping(value = "/ipad/goodbye")
  public String goodbye(){
    return "Goodbye!";
  }
}
