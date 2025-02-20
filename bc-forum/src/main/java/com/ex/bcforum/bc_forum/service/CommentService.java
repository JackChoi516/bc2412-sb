package com.ex.bcforum.bc_forum.service;

import java.util.List;

import com.ex.bcforum.bc_forum.entity.CommentEntity;

public interface CommentService {
  List<CommentEntity> getAllComments();
  List<CommentEntity> getByPostId(Long id);
  CommentEntity addByPostId(Long id, CommentEntity commentEntity);
  CommentEntity setCommentBody(Long id, String body);
}
