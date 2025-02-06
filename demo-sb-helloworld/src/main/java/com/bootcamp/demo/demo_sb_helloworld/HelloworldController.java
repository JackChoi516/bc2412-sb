package com.bootcamp.demo.demo_sb_helloworld;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bootcamp.demo.demo_sb_helloworld.ShoppingMall.Cinema;
import com.bootcamp.demo.demo_sb_helloworld.ShoppingMall.Film;

// Java Object -> JSON (Serialization)
// JSON -> Java Object (Deserialization)

// List & Array (JSON)

// Attribute has no ordering in JSON

@Controller // @GetMapping
@ResponseBody // return JSON
public class HelloworldController {
  // An API for Getting Resource
  @GetMapping(value = "/greeting") // unique
  public String hello(){
    return "Hello World !";
  }

  @GetMapping(value = "/integer")
  public Integer getInteger(){
    return 100;
  }

  @GetMapping(value = "/integers")
  public int[] getIntegers(){
    return new int[]{3, 100, 2};
  }

  @GetMapping(value = "/strings")
  public List<String> getStrings(){
    return Arrays.asList("Vincent", "Lucas");
  }

  @GetMapping(value = "/cat")
  public Cat getCat(){
    return new Cat("John", 3);
  }

  @GetMapping(value = "/cats")
  public List<Cat> getCats(){
    return Arrays.asList(new Cat("John", 3), new Cat("Sally", 4));
  }

  @GetMapping(value = "/dates")
  public List<LocalDate> getDates(){
    return Arrays.asList(LocalDate.of(2024, 11, 2), LocalDate.of(2025, 2, 6), LocalDate.of(2025, 2, 5));
  }

  @GetMapping(value = "/shoppingmall")
  public ShoppingMall getShoppingMall(){
    Film f1 = new Film("film A", "01-Jan-2022");
    Film f2 = new Film("film B", "02-Jan-2022");

    Cinema c1 = new Cinema("Cinema A", "01-Jan-2022", Arrays.asList(f1,f2));
    return new ShoppingMall("K11", 19000, c1, Arrays.asList("Sport", "Food", "Clothing"));
  }

}
