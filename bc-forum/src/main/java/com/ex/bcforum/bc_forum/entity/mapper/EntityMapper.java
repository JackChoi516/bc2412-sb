package com.ex.bcforum.bc_forum.entity.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.ex.bcforum.bc_forum.entity.AddressEntity;
import com.ex.bcforum.bc_forum.entity.CommentEntity;
import com.ex.bcforum.bc_forum.entity.CompanyEntity;
import com.ex.bcforum.bc_forum.entity.GeoEntity;
import com.ex.bcforum.bc_forum.entity.PostEntity;
import com.ex.bcforum.bc_forum.entity.UserEntity;
import com.ex.bcforum.bc_forum.model.dto.CommentDto;
import com.ex.bcforum.bc_forum.model.dto.PostDto;
import com.ex.bcforum.bc_forum.model.dto.UserDto;

@Component
public class EntityMapper {
  public UserEntity map(UserDto userDto){
    return UserEntity.builder().name(userDto.getName()).username(userDto.getUsername()) //
      .email(userDto.getEmail()).phone(userDto.getPhone()).website(userDto.getWebsite()) //
      .build();
  }

  public GeoEntity map(UserDto.Geo geo){
    return GeoEntity.builder().latitude(geo.getLat()).longitude(geo.getLng()) //
      .addressEntities(new ArrayList<>()).build();
  }

  public AddressEntity map(UserDto.Address address){
    return AddressEntity.builder().street(address.getStreet()).suite(address.getSuite()) //
        .city(address.getCity()).zipcode(address.getZipcode()) //
        .userEntity(new ArrayList<>())
        .build();
  }

  public CompanyEntity map(UserDto.Company company){
    return CompanyEntity.builder().name(company.getName()).//
      catchPhrase(company.getCatchPhrase()) //
      .userEntities(new ArrayList<>())
      .bs(company.getBs()).build();
  }

  public PostEntity map(PostDto postDto){
    return PostEntity.builder().title(postDto.getTitle()) //
      .body(postDto.getBody()).comments(new ArrayList<>()).build();
  }

  public CommentEntity map(CommentDto commentDto){
    return CommentEntity.builder().name(commentDto.getName()) //
      .email(commentDto.getEmail()).body(commentDto.getBody()).build();
  }
}
