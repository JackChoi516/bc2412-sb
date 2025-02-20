package com.ex.bcforum.bc_forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.bcforum.bc_forum.entity.CompanyEntity;
import java.util.List;


public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>{
  CompanyEntity findByNameAndCatchPhraseAndBs(String name, String catchPhrase, String bs);
}
