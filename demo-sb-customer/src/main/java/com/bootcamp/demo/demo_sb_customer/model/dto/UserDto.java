package com.bootcamp.demo.demo_sb_customer.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

// ! Data Transfer Object
// This dto is for Deserialization (JSON -> OBJECT)
@Getter
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
  public static class Address {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private Geo geo;
  }

  @Getter
  public static class Company {
    private String name;
    private String catchPhrase;
    private String bs;
  }

  @Getter
  public static class Geo {
    @JsonProperty(value = "lat")
    private String latitude;
    @JsonProperty(value = "lng")
    private String longitude;
  }
}
