package com.bootcamp.demo.demo_sb_restful.controller.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.catalina.valves.LoadBalancerDrainingValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.demo.demo_sb_restful.controller.CatOperation;
import com.bootcamp.demo.demo_sb_restful.model.Cat;
import com.bootcamp.demo.demo_sb_restful.model.CatDatabase;
import com.bootcamp.demo.demo_sb_restful.service.impl.CatServiceImpl;

// ! RESTful API -> GET/POST/DELETE/PUT/PATCH
// Control single resource by GET/POST/DELETE/PUT/PATCH 

// Controller -> The ways to control Cat resource
// insert, update, delete, select
@RestController
public class CatController implements CatOperation {
  // Controller -> Service -> CatDatabase

  // Dependency Injection (Spring Core Concept)
  // Autowired: Try to find an object which fits into catService. (Before Server start complete)
  // ! if fail, server start fail.

  // Field Injection
  @Autowired
  private CatServiceImpl catService;

  // Constructor Injection
  // @Autowired(required = true)
  // public CatController(CatService catService){
  //   this.catService = catService;
  // }

  // insert
  @Override
  public Cat createCat(Cat cat){
    if (catService.put(cat)) // Null pointer exception? Component Scan
      return cat;
    return null;
  }

  // Arrays.asList() vs List.of() vs new ArrayList<>()

  // Get All Cats
  @Override
  public List<Cat> getCats(){
    return Arrays.asList(CatDatabase.HOME);
  }

  // Get Cat by id
  // http://localhost:8082/cat?id=1
  // Deserializtion
  @Override
  public Cat getCat(@RequestParam Long id){
    return this.catService.find(id).orElse(null);
  }

  // http://localhost:8082/cat?id=1
  @Override
  public Boolean deleteCat(@RequestParam Long id){
    return this.catService.delete(id);
  }

  // HashMap.put() -> if exists, override, otherwise, create new
  @Override
  public Boolean updateCat(@RequestParam Long id, @RequestBody Cat cat){
    return this.catService.update(id, cat);
  }

  @Override
  public Boolean patchCatName(@RequestParam Long id, @PathVariable String name){
    return this.catService.patchName(id, name);
  }

}
