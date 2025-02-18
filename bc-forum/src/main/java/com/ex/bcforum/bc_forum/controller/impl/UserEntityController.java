package com.ex.bcforum.bc_forum.controller.impl;

import java.util.List;

import org.hibernate.internal.build.AllowNonPortable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ex.bcforum.bc_forum.controller.UserEntityOperation;
import com.ex.bcforum.bc_forum.entity.UserEntity;
import com.ex.bcforum.bc_forum.service.UserEntityService;
@RestController
public class UserEntityController implements UserEntityOperation{
  @Autowired
  private UserEntityService userEntityService;

  @Override
  public List<UserEntity> getUserEntities(){
    return this.userEntityService.getUserEntities();
  }
}
