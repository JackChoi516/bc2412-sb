package com.ex.bcforum.bc_forum.service;

import java.util.List;

import com.ex.bcforum.bc_forum.entity.PostEntity;

public interface PostService {
  List<PostEntity> getAllPosts();
  List<PostEntity> getByUserId(Long id);
  PostEntity addPost(Long id, PostEntity postEntity);
  PostEntity deleteById(Long id);
}
