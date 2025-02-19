package com.ex.bcforum.bc_forum.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ex.bcforum.bc_forum.service.UserEntityService;

@Component
public class PreServerStartConfig implements CommandLineRunner{
  @Autowired
  private UserEntityService userEntityService;

  @Override  
  public void run(String... args) throws Exception{
    userEntityService.saveUserEntities();
    System.out.println("User data saved.");
  }
}
