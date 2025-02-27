package com.bc2412.demo.web.demo_coin_web.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bc2412.demo.web.demo_coin_web.model.CoinMarket;

public interface CoinService {
  public List<CoinMarket> getCoinMarket();
}
