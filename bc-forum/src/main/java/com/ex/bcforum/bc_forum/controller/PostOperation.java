package com.ex.bcforum.bc_forum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ex.bcforum.bc_forum.codewave.ApiResp;
import com.ex.bcforum.bc_forum.entity.PostEntity;

public interface PostOperation {
  @GetMapping(value = "/posts")
  ApiResp<List<PostEntity>> getAllPosts();

  @GetMapping(value = "/posts/{id}")
  ApiResp<List<PostEntity>> getByUserId(@PathVariable Long id);

  @PostMapping(value = "/post/{id}")
  ApiResp<PostEntity> addPostByUserId(@PathVariable Long id, @RequestBody PostEntity postEntity);
}
