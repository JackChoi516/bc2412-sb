package com.ex.bcforum.bc_forum.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.bcforum.bc_forum.codewave.ApiResp;
import com.ex.bcforum.bc_forum.entity.UserEntity;

public interface UserEntityOperation {
  @GetMapping(value = "/userentities")
  ApiResp<List<UserEntity>> getUserEntities();

  @GetMapping(value = "/userid")
  ApiResp<Optional<UserEntity>> findById(@RequestParam Long id);

  @PutMapping(value = "/user")
  ApiResp<UserEntity> putById(@RequestBody UserEntity userEntity);
}
