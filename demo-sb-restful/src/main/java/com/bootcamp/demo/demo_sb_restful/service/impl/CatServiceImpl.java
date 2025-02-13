package com.bootcamp.demo.demo_sb_restful.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bootcamp.demo.demo_sb_restful.model.Cat;
import com.bootcamp.demo.demo_sb_restful.model.CatDatabase;
import com.bootcamp.demo.demo_sb_restful.service.CatService;

// Person.class -> name -> setName() -> getName()

@Service // Bean
public class CatServiceImpl implements CatService {
  // stateless, can be a bean
  @Override
  public boolean put(Cat cat){
    for (int i = 0; i < CatDatabase.HOME.length; i++){
      if (CatDatabase.HOME[i] == null){
        CatDatabase.HOME[i] = cat;
        return true;
      }
    }
    return false;
  }

  @Override
  public Optional<Cat> find(long catId){
    for (Cat c : CatDatabase.HOME){
      if (c.getId() == catId){
        return Optional.of(c);
      }
    }
    return Optional.empty();
  }

  @Override
  public Boolean delete(Long catId){
    for (int i = 0; i < CatDatabase.HOME.length; i++){
      if (CatDatabase.HOME[i].getId() == catId){
        CatDatabase.HOME[i] = null;
        return true;
      }
    }
    return false;
  }

  @Override
  public Boolean update(Long catId, Cat cat){
    for (int i = 0; i < CatDatabase.HOME.length; i++){
      if (CatDatabase.HOME[i].getId() == catId){
        CatDatabase.HOME[i] = cat;
        return true;
      }
    }
    return false;
  }

  // ! 1. Don't Create Cat, we should find the cat object, call setName().
  // ! 2. Other values of this cat object remain unchange
  @Override
  public Boolean patchName(Long catId, String catName){
    for (Cat cat : CatDatabase.HOME){
      if (cat.getId() == catId){
        cat.setName(catName);
        return true;
      }
    }
    return false;
  }
}
