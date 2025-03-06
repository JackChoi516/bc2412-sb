package com.finance.project.final_project.codewave;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.HttpComponentsClientHttpRequestFactoryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.finance.project.final_project.model.StockDataDto;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.concurrent.TimeUnit;

import org.springframework.http.*;

// @Component
// public class YahooFinanceManager {

//   private final RestTemplate restTemplate;
//   private final CrumbManager crubManager;

//   @Value("${api.yahooFinance.host}")
//   private String host;

//   @Value("${api.yahooFinance.endpoints.data}")
//   private String data;

//   @Value("${api.yahooFinance.endpoints.key}")
//   private String key;

//   public YahooFinanceManager(RestTemplate restTemplate, CrumbManager crubManager) {
//     this.restTemplate = restTemplate;
//     this.crubManager = crubManager;
//   }

//   public StockDataDto getStockDataDto(String symbols) {
//     String url = UriComponentsBuilder.newInstance()
//             .scheme("https")
//             .host(host)
//             .path(data)
//             .queryParam("symbols", symbols)
//             .queryParam("crumb", crubManager.getKey())  // Getting the "crumb" from CrubManager
//             .build().toString();

//     try {
//         // Make a request to get stock data with the valid crumb and cookies
//         return this.restTemplate.getForObject(url, StockDataDto.class);
//     } catch (Exception e) {
//         e.printStackTrace();
//     }
//     return null;
//   }
  
//   // The getKey method here is not needed anymore since CrubManager is now responsible for it.
// }



import com.finance.project.final_project.model.CrumbDto;



import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class YahooFinanceManager {
  private RestTemplate restTemplate;
//   private static final String COOKIE = "B=12345abcde; GUC=AQEBCAFZ...";  // 你的 Cookie
//   private static final String COOKIE = "d=AQABBIFnyWcCEEzSE95xR1t0TfTkRIM4W28FEgEBAQG5ymfTZ2CZyyMA_eMAAA&S=AQAAAoAMEc74g6clnckoe9ltuys";  // 你的 Cookie
//   private CookieStore cookieStore;
  private String cookieA;
  private String cookieB;
  private Long appendNum;
  private StringBuilder stringBuilder;
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

  
//   public YahooFinanceManager() {
//     cookieStore = new BasicCookieStore();  // Initialize CookieStore to hold cookies
//     CloseableHttpClient httpClient = HttpClients.custom()
//             .setDefaultCookieStore(cookieStore)  // Use CookieStore for cookies
//             .setUserAgent(USER_AGENT)
//             .build();
//     HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
//     this.restTemplate = new RestTemplate(factory);
// }

  public YahooFinanceManager(RestTemplate restTemplate){
    this.restTemplate = restTemplate;
    this.stringBuilder = new StringBuilder();
    this.cookieA = "B=12345abcdefg";
    this.cookieB = "; GUC=AQEBCAFZ....";
    this.appendNum = 1L;
  }

  @Value("${api.yahooFinance.host}")
  private String host;
  @Value("${api.yahooFinance.endpoints.data}")
  private String data;
  @Value("${api.yahooFinance.endpoints.key}")
  private String key;

  // public StockDataDto getStockDataDto(String symbols){
  //   String url = UriComponentsBuilder.newInstance() //
  //   .scheme("https").host(host).path(data) //
  //   .queryParam("symbols", symbols) //
  //   .queryParam("crumb", this.getKey()) //
  //   .build().toString();

  //   return this.restTemplate.getForObject(url, StockDataDto.class);
  // }

  public String getStockDataDto(String symbols) {
    String url = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(host)
            .path(data)
            .queryParam("symbols", symbols)
            .queryParam("crumb", this.getKey())
            .build().toString();
    // String url = "https://query1.finance.yahoo.com/v7/finance/quote?symbols=0388.HK&crumb=6bcJj3fXAtN";
    this.setCookieA();
    String newCookie = stringBuilder.append(this.cookieA).append(this.cookieB).toString();
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.USER_AGENT, USER_AGENT);
    headers.set(HttpHeaders.COOKIE, newCookie);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    // try {
    //     // Simple throttle: Delay 1 second between requests
    //     Thread.sleep(1000);
    //     return this.restTemplate.getForObject(url, StockDataDto.class);
    // } catch (InterruptedException e) {
    //     Thread.currentThread().interrupt();
    //     System.err.println("Request was interrupted");
    // } catch (Exception e) {
    //     e.printStackTrace();
    // }
    // return null;

    try {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
  
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // Crumb or required data
        } else {
            System.out.println("Error: " + response.getStatusCode());
        }
  
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    return null;
}

private void setAppend(){
  this.appendNum += 1L;
}
private void setCookieA(){
  this.setAppend();
  this.cookieA = this.stringBuilder.append(this.cookieA).append(this.appendNum).toString();
  System.out.println(this.cookieA + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
}
public String getKey() {
  String url = "https://query1.finance.yahoo.com/v1/test/getcrumb";
  this.setCookieA();
  String newCookie = stringBuilder.append(this.cookieA).append(this.cookieB).toString();
  HttpHeaders headers = new HttpHeaders();
  headers.set(HttpHeaders.USER_AGENT, USER_AGENT);
  headers.set(HttpHeaders.COOKIE, newCookie);
  HttpEntity<String> entity = new HttpEntity<>(headers);

  try {
      ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

      if (response.getStatusCode() == HttpStatus.OK) {
          return response.getBody(); // Crumb or required data
      } else {
          System.out.println("Error: " + response.getStatusCode());
      }

  } catch (Exception e) {
      e.printStackTrace();
      return null;
  }
  return null;
}


  public String getNewCookie() {
    // This could be a call to an authentication or login endpoint
    // that returns a new cookie as part of the response headers.

    String url = "https://query1.finance.yahoo.com/v1/test/getcrumb";  // Change this URL as needed

    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.USER_AGENT, USER_AGENT);
    headers.set(HttpHeaders.COOKIE, "B=12345abcdefggg; GUC=AQEBCAFZ....");

    HttpEntity<String> entity = new HttpEntity<>(headers);

    try {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // Extract the cookie from the response (assuming it's in the headers)
            // String newCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            // return newCookie;
            return response.getBody();
        } else {
            System.out.println("Error retrieving new cookie: " + response.getStatusCode());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

}



