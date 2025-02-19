package com.ex.bcforum.bc_forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.bcforum.bc_forum.entity.PostEntity;
import com.ex.bcforum.bc_forum.entity.UserEntity;

import java.util.List;


public interface PostRepository extends JpaRepository<PostEntity, Long>{
  List<PostEntity> findByUserEntity(UserEntity userEntity);
}
