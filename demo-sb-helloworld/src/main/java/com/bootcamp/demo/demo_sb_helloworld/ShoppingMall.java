package com.bootcamp.demo.demo_sb_helloworld;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ShoppingMall {
  private String name;
  private int area;
  private Cinema cinema;
  private List<String> shopCategory;

  @Getter
  @Builder
  @AllArgsConstructor
public static class Cinema{
  private String name;
  private String openedDate;
  private List<Film> releasedFilms;
}

@Getter
@Builder
@AllArgsConstructor
public static class Film{
  private String name;
  private String releasedDate;
}


}
