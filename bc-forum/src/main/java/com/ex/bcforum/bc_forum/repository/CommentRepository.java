package com.ex.bcforum.bc_forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.bcforum.bc_forum.entity.CommentEntity;
import com.ex.bcforum.bc_forum.entity.PostEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>{
  List<CommentEntity> findByPostEntity(PostEntity postEntity);
}
