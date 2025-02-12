package com.bootcamp.demo.demo_sb_customer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.demo.demo_sb_customer.entity.CompanyEntity;
import com.bootcamp.demo.demo_sb_customer.model.dto.UserDto;
import com.bootcamp.demo.demo_sb_customer.repository.CompanyRepository;
import com.bootcamp.demo.demo_sb_customer.service.CompanyService;
@Service
public class CompanyServiceImpl implements CompanyService{
@Autowired
private CompanyRepository companyRepository;

// @Override
// public CompanyEntity createCompany(UserDto.Company company){
//   CompanyEntity companyEntity = new CompanyEntity();
//   companyEntity.setName(company.getName());
//   companyEntity.setCatchPhrase(company.getCatchPhrase());
//   companyEntity.setBs(company.getBs());
//   this.companyRepository.save(companyEntity);
//   return companyEntity;
// }
  
}
