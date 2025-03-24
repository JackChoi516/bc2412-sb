package com.bootcamp.demo.demo_jmeter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
  public static int availableStock = 1000;
  public static int orderCount = 0;

  @GetMapping(value = "/info")
  public String getInfo(){
    return "Available stock=" + availableStock + ", Order count=" + orderCount;
  }

  @GetMapping(value = "/syncbuy")
  public synchronized String syncbuy(){
    if (availableStock >= 1 && payment()){
      if (availableStock >= 1){
        availableStock--;
        orderCount++;
      }
      return "true, " + getInfo();
    }
    return "false, " + getInfo();
  }

  // @GetMapping(value = "/syncbuy2")
  // public String syncbuy2(){
  //   if (availableStock >= 1 && payment2()){
  //     return "true, " + getInfo();
  //   }
  //   return "false, " + getInfo();
  // }

  // private boolean payment2(){
  //   if (availableStock >= 1){
  //     try {
  //       Thread.sleep(20);
  //       availableStock--;
  //       orderCount++;
  //     } catch (Exception e) {
  //       return false;
  //     }
  //   }
  //   return true;
  // }

  @GetMapping(value = "/buy")
  public String buy(){
    if (availableStock >= 1 && payment()){
      availableStock--;
      orderCount++;
      return "true, " + getInfo();
    }
    return "false, " + getInfo();
  }

  // payment process 20 ms
  private boolean payment(){
    try {
      Thread.sleep(20);
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}
