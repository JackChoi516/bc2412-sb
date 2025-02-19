package com.ex.bcforum.bc_forum.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ex.bcforum.bc_forum.codewave.ApiResp;
import com.ex.bcforum.bc_forum.codewave.SysCode;
import com.ex.bcforum.bc_forum.controller.PostOperation;
import com.ex.bcforum.bc_forum.entity.PostEntity;
import com.ex.bcforum.bc_forum.service.PostService;

@RestController
public class PostController implements PostOperation{
  @Autowired
  private PostService postService;

  @Override
  public ApiResp<List<PostEntity>> getAllPosts(){
    List<PostEntity> result = this.postService.getAllPosts();
    return ApiResp.<List<PostEntity>>builder().sysCode(SysCode.OK).data(result).build();
  }

  @Override
  public ApiResp<List<PostEntity>> getByUserId(Long id){
    List<PostEntity> result = this.postService.getByUserId(id);
    return ApiResp.<List<PostEntity>>builder().sysCode(SysCode.OK).data(result).build();
  }

  @Override
  public ApiResp<PostEntity> addPostByUserId(Long id, PostEntity postEntity){
    return ApiResp.<PostEntity>builder().sysCode(SysCode.OK) //
      .data(this.postService.addPost(id, postEntity)).build();
  }

  @Override
  public ApiResp<PostEntity> deleteById(Long id){
    return ApiResp.<PostEntity>builder().sysCode(SysCode.OK) //
      .data(this.postService.deleteById(id)).build();
  }
}
