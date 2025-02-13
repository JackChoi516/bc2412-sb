package com.ex.bcforum.bc_forum.service;

import java.util.List;

import com.ex.bcforum.bc_forum.dto.UserCommentDTO;
import com.ex.bcforum.bc_forum.dto.UserDTO;

public interface UserService {
  List<UserDTO> getUsers();
  UserCommentDTO getUserComments(Long id);
  
}
