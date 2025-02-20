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

      AddressEntity address = userEntity.getAddressEntity();
      if (address == null){
        throw BusinessException.of(SysCode.INVALID_INPUT);
      }
      // Geo
      GeoEntity geo = userEntity.getAddressEntity().getGeoEntity();
      if (geo == null){
        throw BusinessException.of(SysCode.INVALID_INPUT);
      }
      GeoEntity existingGeo = this.geoRepository //
      .findByLatitudeAndLongitude(geo.getLatitude(), geo.getLongitude());
      if (existingGeo == null){
        geo.getAddressEntities().add(address);
        geo = this.geoRepository.save(geo);
      }else {
        geo = existingGeo;
      }
      address.setGeoEntity(geo);      
      userEntity2.setAddressEntity(address); /////

      // Address
      AddressEntity existingAddress = this.addressRepository.findByStreetAndSuiteAndCityAndZipcode //
      (address.getStreet(), address.getSuite(), address.getCity(), address.getZipcode());
      if (existingAddress != null){
          address = existingAddress;
      }else {
          geo.getAddressEntities().add(address);
          address.setGeoEntity(geo);
          address.getUserEntity().add(userEntity2);
          address = this.addressRepository.save(address);
          }

      // Company
      CompanyEntity company = userEntity.getCompanyEntity();
    
      CompanyEntity existingCompany = this.companyRepository.findByNameAndCatchPhraseAndBs //
      (company.getName(), company.getCatchPhrase(), company.getBs());
      if (existingCompany == null){
        company.getUserEntities().add(userEntity2);
        company = this.companyRepository.save(company);
      }else {
        company = existingCompany;
      }

      userEntity2.setCompanyEntity(company);

    }else {
      throw BusinessException.of(SysCode.USER_NOT_FOUND);
    }
    return this.userRepository.save(userEntity);
  }
  //////////////////////////////
  // @Override
  // @Transactional
  // public UserEntity putById(UserEntity userEntity) {
  //     Optional<UserEntity> target = this.userRepository.findById(userEntity.getId());
  
  //     if (target.isPresent()) {
  //         UserEntity userEntity2 = target.get();
  //         userEntity2.setName(userEntity.getName());
  //         userEntity2.setUsername(userEntity.getUsername());
  //         userEntity2.setEmail(userEntity.getEmail());
  //         userEntity2.setPhone(userEntity.getPhone());
  //         userEntity2.setWebsite(userEntity.getWebsite());
  
  //         // 处理 AddressEntity
  //         AddressEntity address = userEntity.getAddressEntity();
  //         if (address == null) {
  //             throw BusinessException.of(SysCode.INVALID_INPUT);
  //         }
  
  //         // 处理 GeoEntity
  //         GeoEntity geo = address.getGeoEntity();
  //         if (geo != null) {
  //             // 查找或保存 GeoEntity
  //             GeoEntity existingGeo = this.geoRepository.findByLatitudeAndLongitude(geo.getLatitude(), geo.getLongitude());
  //             if (existingGeo == null) {
  //                 geo = this.geoRepository.save(geo); // 新建的 geo
  //             } else {
  //                 geo = existingGeo; // 使用已存在的 geo
  //             }
  //         }
  
  //         // 查找或保存 AddressEntity
  //         AddressEntity existingAddress = this.addressRepository.findByStreetAndSuiteAndCityAndZipcode
  //           (address.getStreet(), address.getSuite(), address.getCity(), address.getZipcode());
  //         if (existingAddress != null) {
  //             address = existingAddress; // 使用已存在的地址
  //             address.getUserEntity().add(userEntity2);
  //         } else {
  //             geo.getAddressEntities().add(address);
  //             address.setGeoEntity(geo);
  //             address.getUserEntity().add(userEntity2);
  //             address = this.addressRepository.save(address); // 确保地址被保存
  //         }
  
  //         userEntity2.setAddressEntity(address); // 设置更新后的 AddressEntity
  
  //         // 处理 CompanyEntity
  //         CompanyEntity companyEntity = userEntity.getCompanyEntity();
  //         if (companyEntity != null) {
  //             CompanyEntity existingCompany = this.companyRepository.findByNameAndCatchPhraseAndBs
  //               (companyEntity.getName(), companyEntity.getCatchPhrase(), companyEntity.getBs());
  //             if (existingCompany == null) {
  //               companyEntity.getUserEntities().add(userEntity2);
  //                 companyEntity = this.companyRepository.save(companyEntity); // 新建的 company
  //             } else {
  //                 companyEntity = existingCompany; // 使用已存在的公司
  //                 companyEntity.getUserEntities().add(userEntity2);
  //             }
  //             userEntity2.setCompanyEntity(companyEntity);
  //         }
  
  //         return this.userRepository.save(userEntity2); // 返回更新后的 userEntity
  //     } else {
  //         throw BusinessException.of(SysCode.USER_NOT_FOUND);
  //     }
  // }
}
