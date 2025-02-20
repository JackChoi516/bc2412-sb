package com.ex.bcforum.bc_forum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.bcforum.bc_forum.codewave.ApiResp;
import com.ex.bcforum.bc_forum.entity.CommentEntity;

public interface CommentOperation {
  @GetMapping(value = "/comments")
  ApiResp<List<CommentEntity>> getAllComments();

  @GetMapping(value = "/comments/postid")
  ApiResp<List<CommentEntity>> getByPostId(@RequestParam Long id);

  @PostMapping(value = "/comment/postid")
  ApiResp<CommentEntity> addByPostId(@RequestParam Long id, @RequestBody CommentEntity commentEntity);

  @PatchMapping(value = "/commentbody/commentid")
  ApiResp<CommentEntity> setCommentBody(@RequestParam Long id, @RequestBody String body);
}
