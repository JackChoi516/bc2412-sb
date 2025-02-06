package com.bootcamp.demo.demo_sb_helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class HelloworldController {
  // An API for Getting Resource
  @GetMapping(value = "/greeting") // unique
  public String hello(){
    return "Hello World !";
  }

  @GetMapping(value = "/Integer")
  public Integer getInteger(){
    return 100;
  }

  @GetMapping(value = "/integers")
  public int[] getIntegers(){
    return new int[]{3, 100, 2};
  }

  

}
