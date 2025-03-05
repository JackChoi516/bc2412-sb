package com.bc2412.demo.web.demo_coin_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bc2412.demo.web.demo_coin_web.dto.CalculatorResult;
import com.bc2412.demo.web.demo_coin_web.model.CoinMarket;
import com.bc2412.demo.web.demo_coin_web.service.CoinService;
// @RestController // return JSON as response
@Controller
public class ViewController {
  @Autowired
  private CoinService coinService;

  @GetMapping(value = "/bootcamp")
  // select from DB
  // put the list into model
  public String sayHelloPage(Model model){
    model.addAttribute("tutor", "vincent");
    return "hello"; // html file name
  }

  // price change 24h 3.23% (Green)
  // price change 24h -1.23% (Red)
  @GetMapping(value = "/coins")
  public String coinPage(Model model){
    List<CoinMarket> response = this.coinService.getCoinMarket();
    model.addAttribute("coinList", response);
    return "coin";
  }

}
