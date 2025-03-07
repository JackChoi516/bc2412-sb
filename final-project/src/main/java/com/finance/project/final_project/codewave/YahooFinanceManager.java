package com.finance.project.final_project.codewave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.finance.project.final_project.model.StockDataDto;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;

public class YahooFinanceManager {
  private RestTemplate restTemplate;
  private StringBuilder cookieA;
  private StringBuilder cookieB;
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

  public YahooFinanceManager(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.cookieA = new StringBuilder("B=12345abcdefg");
    this.cookieB = new StringBuilder("; GUC=AQEBCAFZ....");
  }

  @Value("${api.yahooFinance.host}")
  private String host;
  @Value("${api.yahooFinance.endpoints.data}")
  private String data;
  @Value("${api.yahooFinance.endpoints.key}")
  private String key;
  @Autowired
  private ObjectMapper objectMapper;

  public StockDataDto getStockDataDto(String symbols) {
    String url = UriComponentsBuilder.newInstance()
        .scheme("https")
        .host(host)
        .path(data)
        .queryParam("symbols", symbols)
        .queryParam("crumb", this.getKey())
        .build().toString();
    System.out.println(url + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    this.setCookieA();
    String newCookie = this.cookieA.append(this.cookieB).toString();
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.USER_AGENT, USER_AGENT);
    headers.set(HttpHeaders.COOKIE, newCookie);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    try {
      ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        return this.objectMapper.readValue(response.getBody(), StockDataDto.class); // Crumb or required data
      } else {
        System.out.println("Error: " + response.getStatusCode());
      }

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return null;
  }

  private void setCookieA() {
    String randomCookie = UUID.randomUUID().toString();
    this.cookieA.append(randomCookie);
    // System.out.println(randomCookie + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
  }

  public String getKey() {
    String url = UriComponentsBuilder.newInstance().scheme("https").host(host).path(key).build().toString();
    this.setCookieA();
    String newCookie = this.cookieA.append(this.cookieB).toString();
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.USER_AGENT, USER_AGENT);
    headers.set(HttpHeaders.COOKIE, newCookie);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    try {
      ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        return response.getBody(); 
      } else {
        System.out.println("Error: " + response.getStatusCode());
      }

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return null;
  }

}
