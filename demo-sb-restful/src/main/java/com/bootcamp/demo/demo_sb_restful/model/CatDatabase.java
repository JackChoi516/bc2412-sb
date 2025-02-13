package com.bootcamp.demo.demo_sb_restful.model;

import java.util.Optional;

public class CatDatabase {
  public static final Cat[] HOME = new Cat[5];

  // public static Optional<Cat> find(long catId){
  //   for (Cat c : HOME){
  //     if (c.getId() == catId){
  //       return Optional.of(c);
  //     }
  //   }
  //   return Optional.empty();
  // }

  // public static Boolean delete(Long catId){
  //   for (int i = 0; i < HOME.length; i++){
  //     if (HOME[i].getId() == catId){
  //       HOME[i] = null;
  //       return true;
  //     }
  //   }
  //   return false;
  // }

  // public static Boolean update(Long catId, Cat cat){
  //   for (int i = 0; i < HOME.length; i++){
  //     if (HOME[i].getId() == catId){
  //       HOME[i] = cat;
  //       return true;
  //     }
  //   }
  //   return false;
  // }

  // // ! 1. Don't Create Cat, we should find the cat object, call setName().
  // // ! 2. Other values of this cat object remain unchange
  // public static Boolean patchName(Long catId, String catName){
  //   for (Cat cat : HOME){
  //     if (cat.getId() == catId){
  //       cat.setName(catName);
  //       return true;
  //     }
  //   }
  //   return false;
  // }
}
