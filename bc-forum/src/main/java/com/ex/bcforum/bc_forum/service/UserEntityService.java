package com.ex.bcforum.bc_forum.service;

import java.util.List;
import java.util.Optional;

import com.ex.bcforum.bc_forum.entity.UserEntity;

public interface UserEntityService {
  void saveUserEntities();
  ////
  List<UserEntity> getUserEntities();
  Optional<UserEntity> findById(Long id);
  UserEntity putById(UserEntity userEntity);
}
