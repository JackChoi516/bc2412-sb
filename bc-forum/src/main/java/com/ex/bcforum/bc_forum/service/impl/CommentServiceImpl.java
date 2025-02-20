package com.ex.bcforum.bc_forum.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ex.bcforum.bc_forum.codewave.BusinessException;
import com.ex.bcforum.bc_forum.codewave.SysCode;
import com.ex.bcforum.bc_forum.entity.CommentEntity;
import com.ex.bcforum.bc_forum.entity.PostEntity;
import com.ex.bcforum.bc_forum.repository.CommentRepository;
import com.ex.bcforum.bc_forum.repository.PostRepository;
import com.ex.bcforum.bc_forum.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private PostRepository postRepository;

  @Override
  public List<CommentEntity> getAllComments(){
    return this.commentRepository.findAll();
  }

  @Override
  public List<CommentEntity> getByPostId(Long id){
    PostEntity post = this.postRepository.findById(id) //
      .orElseThrow(() -> BusinessException.of(SysCode.POST_NOT_FOUND));
    return this.commentRepository.findByPostEntity(post);
  }

  @Override
  public CommentEntity addByPostId(Long id, CommentEntity commentEntity){
    PostEntity post = this.postRepository.findById(id) //
      .orElseThrow(() -> BusinessException.of(SysCode.POST_NOT_FOUND));

    commentEntity.setPostEntity(post);
    return this.commentRepository.save(commentEntity);
  }

  @Override
  @Transactional
  public CommentEntity setCommentBody(Long id, String body){
    CommentEntity comment = this.commentRepository.findById(id) //
      .orElseThrow(() -> BusinessException.of(SysCode.COMMENT_NOT_FOUND));

    comment.setBody(body);
    return comment;
  }
}
