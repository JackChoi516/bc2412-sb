package com.ex.bcforum.bc_forum.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ex.bcforum.bc_forum.codewave.BusinessException;
import com.ex.bcforum.bc_forum.codewave.SysCode;
import com.ex.bcforum.bc_forum.entity.PostEntity;
import com.ex.bcforum.bc_forum.entity.UserEntity;
import com.ex.bcforum.bc_forum.repository.PostRepository;
import com.ex.bcforum.bc_forum.repository.UserRepository;
import com.ex.bcforum.bc_forum.service.PostService;

@Service
public class PostServiceImpl implements PostService{
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private UserRepository userRepository;

  @Override
  public List<PostEntity> getAllPosts(){
    return this.postRepository.findAll();
  }

  @Override
  public List<PostEntity> getByUserId(Long id){
    UserEntity userEntity = this.userRepository.findById(id) //
      .orElseThrow(() -> BusinessException.of(SysCode.USER_NOT_FOUND));

    return this.postRepository.findByUserEntity(userEntity);
  }

  @Override
  public PostEntity addPost(Long id, PostEntity postEntity){
    UserEntity userEntity = this.userRepository.findById(id) //
      .orElseThrow(() -> BusinessException.of(SysCode.USER_NOT_FOUND));
    
    userEntity.getPostEntities().add(postEntity);
    postEntity.setUserEntity(userEntity);
    return this.postRepository.save(postEntity);
  }

  @Override
  public PostEntity deleteById(Long id){
    Optional<PostEntity> post = this.postRepository.findById(id);
    if (post.isPresent()){
      this.postRepository.deleteById(id);
    }else {
      throw BusinessException.of(SysCode.POST_NOT_FOUND);
    }
    return post.get();
  }
}
