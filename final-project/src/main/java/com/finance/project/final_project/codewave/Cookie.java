package com.finance.project.final_project.codewave;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.finance.project.final_project.model.StockDataDto;

public class Cookie {
  @Autowired
  private RestTemplate restTemplate;

  public StockDataDto getKeyNoCookie(@PathVariable String stockerNum) {
    String url = "https://fc.yahoo.com";
    // RestTemplate restTemplate = new RestTemplate();

    // 設定 request headers
    HttpHeaders headers = new org.springframework.http.HttpHeaders();
    headers.set("User-Agent", "Mozilla/5.0");

    HttpEntity<String> entity = new HttpEntity<>(headers);
    List<String> cookies = null;
    try {
      ResponseEntity<String> response =
          restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
      System.out.println("Response body: " + response.getBody());
      System.out.println("Response headers: " + response.getHeaders());
    } catch (HttpClientErrorException | HttpServerErrorException ex) {
      cookies = ex.getResponseHeaders().get(HttpHeaders.SET_COOKIE);
    }

    String crumbUrl = "https://query1.finance.yahoo.com/v1/test/getcrumb";
    headers.set("User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
    headers.set("Cookie", String.join("", cookies));

    ResponseEntity<String> crumbResponse =
        restTemplate.exchange(crumbUrl, HttpMethod.GET, entity, String.class);
    String crumb = crumbResponse.getBody();
    System.out.println(crumb);

    String dataUrl = String.format(
        "https://query1.finance.yahoo.com/v7/finance/quote?symbols=%s.HK&crumb=%s",
        stockerNum, crumb);

        StockDataDto finalResponse = restTemplate
        .exchange(dataUrl, HttpMethod.GET, entity, StockDataDto.class)
        .getBody();

    return finalResponse;
  }
}
