package com.bootcamp.demo.demo_sb_customer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// ! Different numbers of fields for the API

// This DTO is for Serialization
@Getter
@Builder
@AllArgsConstructor
public class UserDTO {
  private Long id;
  private String name;
  private String username;
  private String email;
  private Address address;

  @Getter
  @AllArgsConstructor
  public static class Address {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private Geo geo;
  }

  @Getter
  @AllArgsConstructor
  public static class Geo {
    @JsonProperty(value = "x")
    private String latitude;
    @JsonProperty(value = "y")
    private String longitude;
  }
}
