package com.ex.bcforum.bc_forum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import com.ex.bcforum.bc_forum.entity.UserEntity;

public interface UserEntityOperation {
  @GetMapping(value = "/userentities")
  List<UserEntity> getUserEntities();
}
