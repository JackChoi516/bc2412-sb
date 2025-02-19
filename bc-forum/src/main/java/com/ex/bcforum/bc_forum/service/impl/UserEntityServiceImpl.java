package com.ex.bcforum.bc_forum.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ex.bcforum.bc_forum.codewave.BusinessException;
import com.ex.bcforum.bc_forum.codewave.SysCode;
import com.ex.bcforum.bc_forum.entity.AddressEntity;
import com.ex.bcforum.bc_forum.entity.CommentEntity;
import com.ex.bcforum.bc_forum.entity.CompanyEntity;
import com.ex.bcforum.bc_forum.entity.GeoEntity;
import com.ex.bcforum.bc_forum.entity.PostEntity;
import com.ex.bcforum.bc_forum.entity.UserEntity;
import com.ex.bcforum.bc_forum.entity.mapper.EntityMapper;
import com.ex.bcforum.bc_forum.model.dto.CommentDto;
import com.ex.bcforum.bc_forum.model.dto.PostDto;
import com.ex.bcforum.bc_forum.model.dto.UserDto;
import com.ex.bcforum.bc_forum.repository.AddressRepository;
import com.ex.bcforum.bc_forum.repository.CommentRepository;
import com.ex.bcforum.bc_forum.repository.CompanyRepository;
import com.ex.bcforum.bc_forum.repository.GeoRepository;
import com.ex.bcforum.bc_forum.repository.PostRepository;
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
  private CommentRepository commentRepository;
  @Autowired
  private PostRepository postRepository;

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
  @Transactional
  public void saveUserEntities(){
    if (this.userRepository.count() == 0){
    String usersUrl = UriComponentsBuilder.newInstance() //
      .scheme("https").host(domain).path(usersEndpoint).build().toString();

    List<UserDto> userDtos = Arrays.asList(this.restTemplate.getForObject(usersUrl, UserDto[].class));

    // this.commentRepository.deleteAll();
    // this.postRepository.deleteAll();
    // this.userRepository.deleteAll();
    // this.companyRepository.deleteAll();
    // this.addressRepository.deleteAll();
    // this.geoRepository.deleteAll();

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

    });

    // comments and posts
    String commentsUrl = UriComponentsBuilder.newInstance() //
    .scheme("https").host(domain).path(commentsEndpoint).build().toString();
    String postsUrl = UriComponentsBuilder.newInstance() //
    .scheme("https").host(domain).path(postsEndpoint).build().toString();
    List<CommentDto> commentDtos = Arrays.asList(this.restTemplate.getForObject(commentsUrl, CommentDto[].class));
    List<PostDto> postDtos = Arrays.asList(this.restTemplate.getForObject(postsUrl, PostDto[].class));

    postDtos.stream().forEach(e -> {
      PostEntity postEntity = entityMapper.map(e);
      Optional<UserEntity> targetUser = this.userRepository.findById(e.getUserId());
      if (targetUser.isPresent()){
        targetUser.get().getPostEntities().add(postEntity);
        postEntity.setUserEntity(targetUser.get());
      }
      this.postRepository.save(postEntity);
    });


    commentDtos.stream().forEach(e -> {
      CommentEntity commentEntity = entityMapper.map(e);
      Optional<PostEntity> targetPost = this.postRepository.findById(e.getPostId());
      if (targetPost.isPresent()){
        targetPost.get().getComments().add(commentEntity);
        commentEntity.setPostEntity(targetPost.get());
      }
      this.commentRepository.save(commentEntity);
    });
  }
  }

  @Override
  public List<UserEntity> getUserEntities(){
    return this.userRepository.findAll();
  }

  @Override
  public Optional<UserEntity> findById(Long id){
    return this.userRepository.findById(id);
  }

  @Override
  @Transactional
  public UserEntity putById(UserEntity userEntity){
    Optional<UserEntity> target = this.userRepository.findById(userEntity.getId());
    if (target.isPresent()){
      UserEntity userEntity2 = target.get();
      userEntity2.setName(userEntity.getName());
      userEntity2.setUsername(userEntity.getUsername());
      userEntity2.setEmail(userEntity.getEmail());
      userEntity2.setPhone(userEntity.getPhone());
      userEntity2.setWebsite(userEntity.getWebsite());
      // boolean foundGeo = false;
      // GeoEntity geo = userEntity.getAddressEntity().getGeoEntity();
      // for (GeoEntity g : this.geoRepository.findAll()){
      //   if (userEntity.getAddressEntity().getGeoEntity().equals(g)){
      //     foundGeo = true;
      //     geo = g;
      //   }
      // }
      // if (foundGeo == false){
      //   this.geoRepository.save(geo);
      // }
      boolean foundAddress = false;
      AddressEntity address = userEntity.getAddressEntity();
      for (AddressEntity a :this.addressRepository.findAll()){
        if (a.equals(userEntity.getAddressEntity())){
          foundAddress = true;
          address = a;
        }
      }
      if (foundAddress == false){
        // address.setGeoEntity(geo);
        this.addressRepository.save(address);
      }
      userEntity2.setAddressEntity(address); ///
      // GeoEntity geo = userEntity.getAddressEntity().getGeoEntity();
      System.out.println( userEntity.getAddressEntity().getGeoEntity());
      CompanyEntity companyEntity = userEntity.getCompanyEntity();
      boolean foundCompany = false;
      for (CompanyEntity c : this.companyRepository.findAll()){
        if (c.equals(companyEntity)){
          companyEntity = c;
          foundCompany = true;
        }
      }
      if (foundCompany == false){
        this.companyRepository.save(companyEntity);
      }
      userEntity2.setCompanyEntity(companyEntity);
    }else {
      throw BusinessException.of(SysCode.USER_NOT_FOUND);
    }
    return this.userRepository.save(userEntity);
  }
}
