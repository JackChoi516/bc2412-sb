package com.bootcamp.demo.demo_sb_restful.service;

import java.util.Optional;

import com.bootcamp.demo.demo_sb_restful.model.Cat;
import com.bootcamp.demo.demo_sb_restful.model.CatDatabase;

public interface CatService {

  public boolean put(Cat cat);

  public Optional<Cat> find(long catId);

  public Boolean delete(Long catId);

  public Boolean update(Long catId, Cat cat);

  public Boolean patchName(Long catId, String catName);
}

