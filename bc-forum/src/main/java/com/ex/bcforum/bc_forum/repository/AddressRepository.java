package com.ex.bcforum.bc_forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.bcforum.bc_forum.entity.AddressEntity;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
  
}
