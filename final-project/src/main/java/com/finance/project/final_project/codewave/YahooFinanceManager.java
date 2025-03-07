package com.finance.project.final_project.codewave;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.finance.project.final_project.model.StockDataDto;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class YahooFinanceManager {
  private RestTemplate restTemplate;
  private String yahooCookie;
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

  public YahooFinanceManager(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.yahooCookie = "";
  }

  @Value("${api.yahooFinance.host}")
  private String host;
  @Value("${api.yahooFinance.endpoints.data}")
  private String data;
  @Value("${api.yahooFinance.endpoints.key}")
  private String key;

  public StockDataDto getStockDataDto(String symbols) throws Exception{
 
    String url = UriComponentsBuilder.newInstance()
    .scheme("https")
    .host(host)
    .path(data)
    .queryParam("symbols", symbols)
    .queryParam("crumb", this.getKey())
    .build().toString();

    org.springframework.http.HttpHeaders headers2 = new org.springframework.http.HttpHeaders();
    headers2.set(org.springframework.http.HttpHeaders.USER_AGENT, USER_AGENT);
    headers2.set(org.springframework.http.HttpHeaders.COOKIE, this.yahooCookie);
    HttpEntity<String> entity = new HttpEntity<>(headers2);
    
    try {
    ResponseEntity<StockDataDto> response2 = restTemplate.exchange(url,
    HttpMethod.GET, entity, StockDataDto.class);
    if (response2.getStatusCode() == HttpStatus.OK) {
    return response2.getBody();
    } else {
    System.out.println("Error: " + response2.getStatusCode());
    }
    } catch (Exception e) {
    e.printStackTrace();
    return null;
    }
    return null;
  }


  public String getKey() throws Exception{
    String url = UriComponentsBuilder.newInstance().scheme("https").host(host).path(key).build().toString();

    String url2 = "https://fc.yahoo.com";
    HttpClient client = java.net.http.HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(url2))
        .header("User-Agent", "MyCustomUserAgent/1.0")
        .build();
    HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());

    HttpHeaders headers2 = response2.headers();

    List<String> cookies = headers2.allValues("Set-Cookie");
    String newCookie = cookies.get(0);
    this.yahooCookie = newCookie;
    System.out.println("!!!!!!!" + this.yahooCookie + "!!!!!!!!!");
    org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
    headers.set(org.springframework.http.HttpHeaders.USER_AGENT, USER_AGENT);
    headers.set(org.springframework.http.HttpHeaders.COOKIE, newCookie);
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
