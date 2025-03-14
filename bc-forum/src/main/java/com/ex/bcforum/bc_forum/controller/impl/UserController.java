package com.ex.bcforum.bc_forum.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ex.bcforum.bc_forum.controller.UserOperation;
import com.ex.bcforum.bc_forum.dto.UserCommentDTO;
import com.ex.bcforum.bc_forum.dto.UserDTO;
import com.ex.bcforum.bc_forum.service.UserService;
import com.ex.bcforum.bc_forum.service.impl.UserServiceImpl;

@RestController
public class UserController implements UserOperation{
  @Autowired
  private UserService UserService;

  @Override
  public List<UserDTO> getUsers(){
    return this.UserService.getUsers();
  }

  @Override
  public UserCommentDTO getUserComments(Long id){
    return this.UserService.getUserComments(id);
  }
  
}
