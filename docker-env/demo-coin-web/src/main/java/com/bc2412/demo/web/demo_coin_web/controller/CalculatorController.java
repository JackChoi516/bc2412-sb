package com.bc2412.demo.web.demo_coin_web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bc2412.demo.web.demo_coin_web.dto.CalculatorResult;

import jakarta.websocket.server.PathParam;

@RestController
public class CalculatorController {
  @Autowired
  private RestTemplate restTemplate;

  @Value("${api.calculater.host}")
  private String host;

  @Value("${api.calculater.endpoint}")
  private String endpoint;

  @GetMapping(value = "/calculator/{x}/{y}")
  public Integer calculate(@PathVariable Integer x, @PathVariable Integer y){
    String url = "http://" + host + endpoint;
    url = url.replace("{x}", String.valueOf(x));
    url = url.replace("{y}", String.valueOf(y));
    System.out.println("url=" + url + "!!!!!!!!!!!!!!!!!");
    return this.restTemplate.getForObject(url, CalculatorResult.class).getValue();
  }

}
