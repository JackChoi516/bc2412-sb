package com.bootcamp.demo.demo_sb_customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcamp.demo.demo_sb_customer.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  
}
