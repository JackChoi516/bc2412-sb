package com.ex.bcforum.bc_forum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.bcforum.bc_forum.dto.UserCommentDTO;
import com.ex.bcforum.bc_forum.dto.UserDTO;

public interface UserOperation {
  @GetMapping(value = "/users")
  List<UserDTO> getUsers();

  @GetMapping(value = "/usercomments")
  UserCommentDTO getUserComments(@RequestParam Long id);
}
