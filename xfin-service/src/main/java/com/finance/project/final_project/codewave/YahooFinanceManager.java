package com.finance.project.final_project.codewave;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import com.finance.project.final_project.model.QuoteDataDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

public class YahooFinanceManager {
  private RestTemplate restTemplate;
  private CookieStore cookieStore;
  private static final String USER_AGENT = //
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
  private HttpHeaders headers;
  private HttpEntity<String> entity;

  public YahooFinanceManager(RestTemplate restTemplate, CookieStore cookieStore) {
    this.restTemplate = restTemplate;
    this.cookieStore = cookieStore;
    this.headers = new HttpHeaders();
    this.headers.set("User-Agent", USER_AGENT);
    this.entity = new HttpEntity<>(headers);
  }

  @Value("${api.yahooFinance.host}")
  private String host;
  @Value("${api.yahooFinance.endpoints.data}")
  private String data;
  @Value("${api.yahooFinance.endpoints.key}")
  private String crumEndpoint;

  public QuoteDataDto getQuoteDataDto(String symbols){
 
    String url = UriComponentsBuilder.newInstance()
    .scheme("https")
    .host(host)
    .path(data)
    .queryParam("symbols", symbols)
    .queryParam("crumb", this.getCrumb())
    .build().toString();
    
    ResponseEntity<QuoteDataDto> response = restTemplate //
      .exchange(url, HttpMethod.GET, entity, QuoteDataDto.class);

    cookieStore.clear();
    return response.getBody();
  }


  public String getCrumb(){
    String url = UriComponentsBuilder.newInstance() //
      .scheme("https").host(host).path(crumEndpoint).build().toString();

    cookieStore.clear();
    String url2 = "https://fc.yahoo.com";
    
    try {
      String response = restTemplate.getForObject(url2, String.class);
    } catch (Exception e) {
      System.out.println("Cookie retrieved.");
    }

    ResponseEntity<String> response = restTemplate //
      .exchange(url, HttpMethod.GET, entity, String.class);
    return response.getBody();
  }

}
