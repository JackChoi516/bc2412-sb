package com.bc2412.demo.web.demo_coin_web.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bc2412.demo.web.demo_coin_web.model.CoinMarket;
import com.bc2412.demo.web.demo_coin_web.service.CoinService;

@Service
public class CoinServiceImpl implements CoinService {
  @Autowired
  private RestTemplate restTemplate;

  public List<CoinMarket> getCoinMarket(){
    String url = "http://localhost:8105/market";

    return Arrays.asList(this.restTemplate.getForObject(url, CoinMarket[].class));
  }
}
