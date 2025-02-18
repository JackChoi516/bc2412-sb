package com.ex.bcforum.bc_forum.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ex.bcforum.bc_forum.entity.AddressEntity;
import com.ex.bcforum.bc_forum.entity.CompanyEntity;
import com.ex.bcforum.bc_forum.entity.GeoEntity;
import com.ex.bcforum.bc_forum.entity.UserEntity;
import com.ex.bcforum.bc_forum.entity.mapper.EntityMapper;
import com.ex.bcforum.bc_forum.model.dto.CommentDto;
import com.ex.bcforum.bc_forum.model.dto.PostDto;
import com.ex.bcforum.bc_forum.model.dto.UserDto;
import com.ex.bcforum.bc_forum.repository.AddressRepository;
import com.ex.bcforum.bc_forum.repository.CompanyRepository;
import com.ex.bcforum.bc_forum.repository.GeoRepository;
import com.ex.bcforum.bc_forum.repository.UserRepository;
import com.ex.bcforum.bc_forum.service.UserEntityService;

import jakarta.persistence.EntityManager;
@Service
public class UserEntityServiceImpl implements UserEntityService{
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private GeoRepository geoRepository;

  @Autowired
  private EntityMapper entityMapper;

  @Autowired
  private RestTemplate restTemplate;

  @Value("${api.jsonplaceholder.domain}")
  private String domain;

  @Value("${api.jsonplaceholder.endpoints.users}")
  private String usersEndpoint;

  @Value("${api.jsonplaceholder.endpoints.posts}")
  private String postsEndpoint;

  @Value("${api.jsonplaceholder.endpoints.comments}")
  private String commentsEndpoint;

  @Override
  public List<UserEntity> getUserEntities(){
    List<UserEntity> result = new ArrayList<>();
    String usersUrl = UriComponentsBuilder.newInstance() //
      .scheme("https").host(domain).path(usersEndpoint).build().toString();

    List<UserDto> userDtos = Arrays.asList(this.restTemplate.getForObject(usersUrl, UserDto[].class));

    this.userRepository.deleteAll();
    this.companyRepository.deleteAll();
    this.addressRepository.deleteAll();
    this.geoRepository.deleteAll();

    userDtos.stream().forEach(e -> {
      UserEntity userEntity = (this.entityMapper.map(e));

      CompanyEntity companyEntity = this.companyRepository.save(this.entityMapper.map(e.getCompany()));
      companyEntity.getUserEntities().add(userEntity);

      GeoEntity geoEntity = geoRepository.save(entityMapper.map(e.getAddress().getGeo()));
      AddressEntity addressEntity = this.addressRepository.save(entityMapper.map(e.getAddress()));
      geoEntity.getAddressEntities().add(addressEntity);
      addressEntity.setGeoEntity(geoEntity);

      userEntity.setAddressEntity(addressEntity);
      userEntity.setCompanyEntity(companyEntity);
      this.userRepository.save(userEntity);

      result.add(userEntity);
      
    });

    // comment
    String commentsUrl = UriComponentsBuilder.newInstance() //
    .scheme("https").host(domain).path(commentsEndpoint).build().toString();
    String postsUrl = UriComponentsBuilder.newInstance() //
    .scheme("https").host(domain).path(postsEndpoint).build().toString();
    List<CommentDto> commentDtos = Arrays.asList(this.restTemplate.getForObject(commentsUrl, CommentDto[].class));
    List<PostDto> postDtos = Arrays.asList(this.restTemplate.getForObject(postsUrl, PostDto[].class));

    postDtos.stream().forEach(e -> {
      entityMapper.map(e);
    
    });

    return result;
  }
}
