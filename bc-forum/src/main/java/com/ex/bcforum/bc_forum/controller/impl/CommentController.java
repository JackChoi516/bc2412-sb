package com.ex.bcforum.bc_forum.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ex.bcforum.bc_forum.codewave.ApiResp;
import com.ex.bcforum.bc_forum.codewave.SysCode;
import com.ex.bcforum.bc_forum.controller.CommentOperation;
import com.ex.bcforum.bc_forum.entity.CommentEntity;
import com.ex.bcforum.bc_forum.service.CommentService;

@RestController
public class CommentController implements CommentOperation{
  @Autowired
  private CommentService commentService;

  @Override
  public ApiResp<List<CommentEntity>> getAllComments(){
    return ApiResp.<List<CommentEntity>>builder() //
      .sysCode(SysCode.OK).data(this.commentService.getAllComments()).build();
  }

  @Override
  public ApiResp<List<CommentEntity>> getByPostId(Long id){
    return ApiResp.<List<CommentEntity>>builder() //
      .sysCode(SysCode.OK).data(this.commentService.getByPostId(id)).build();
  }

  @Override
  public ApiResp<CommentEntity> addByPostId(Long id, CommentEntity commentEntity){
    return ApiResp.<CommentEntity>builder().sysCode(SysCode.OK) //
      .data(this.commentService.addByPostId(id, commentEntity)).build();
  }

  @Override
  public ApiResp<CommentEntity> setCommentBody(Long id, String body){
    return ApiResp.<CommentEntity>builder().sysCode(SysCode.OK) //
      .data(this.commentService.setCommentBody(id, body)).build();
  }
}
