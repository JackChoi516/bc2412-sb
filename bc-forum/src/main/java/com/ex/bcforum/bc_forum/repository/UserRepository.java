package com.ex.bcforum.bc_forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.bcforum.bc_forum.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
  
}
