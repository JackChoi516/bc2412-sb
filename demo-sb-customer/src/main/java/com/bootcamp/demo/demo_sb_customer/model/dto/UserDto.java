package com.bootcamp.demo.demo_sb_customer.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

// ! Data Transfer Object
// This dto is for Deserialization (JSON -> OBJECT)
@Getter
@Builder
public class UserDto {
  private Long id;
  private String name;
  private String username;
  private String email;
  private Address address;
  private String phone;
  private String website;
  private Company company;

  @Getter
  @Builder
  public static class Address {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private Geo geo;
  }

  @Getter
  @Builder
  public static class Company {
    private String name;
    private String catchPhrase;
    private String bs;
  }

  @Getter
  @Builder
  public static class Geo {
    @JsonProperty(value = "lat")
    private String latitude;
    @JsonProperty(value = "lng")
    private String longitude;
  }
}
