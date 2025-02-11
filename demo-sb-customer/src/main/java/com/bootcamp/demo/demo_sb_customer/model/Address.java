package com.bootcamp.demo.demo_sb_customer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Address {
  private String street;
  private String suite;
  private String city;
  private String zipcode;
  private Geo geo;
}
