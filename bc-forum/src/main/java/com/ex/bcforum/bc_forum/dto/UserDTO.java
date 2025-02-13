package com.ex.bcforum.bc_forum.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class UserDTO {
  private Long id;
  private String name;
  private String userName;
  private String email;
  private Address address;
  private String phone;
  private String webSite;
  private Company company;
  private List<Post> posts;

  @Getter
  @Setter
  @Builder
  public static class Address{
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private Geo geo;
  }

  @Getter
  @Setter
  @Builder
  public static class Geo{
    private Double lat;
    private Double lng;
  }

  @Getter
  @Setter
  @Builder
  public static class Company{
    private String name;
    private String catchPhrase;
    private String bs;
  }

  @Getter
  @Setter
  @Builder
  public static class Post{
    private Long id;
    private String title;
    private String body;
    private List<Comment> comments;
  }

  @Getter
  @Setter
  @Builder
  public static class Comment{
    private Long id;
    private String name;
    private String email;
    private String body;
  }
}
