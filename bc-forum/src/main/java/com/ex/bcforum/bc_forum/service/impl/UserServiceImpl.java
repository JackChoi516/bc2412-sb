package com.ex.bcforum.bc_forum.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ex.bcforum.bc_forum.codewave.BusinessException;
import com.ex.bcforum.bc_forum.codewave.SysCode;
import com.ex.bcforum.bc_forum.dto.UserCommentDTO;
import com.ex.bcforum.bc_forum.dto.UserDTO;
import com.ex.bcforum.bc_forum.model.dto.CommentDto;
import com.ex.bcforum.bc_forum.model.dto.PostDto;
import com.ex.bcforum.bc_forum.model.dto.UserDto;
import com.ex.bcforum.bc_forum.service.UserService;

@Service
public class UserServiceImpl implements UserService{
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
  public List<UserDTO> getUsers(){
    List<UserDTO> userDTOs = new ArrayList<>();
    String commentsUrl = UriComponentsBuilder.newInstance() //
      .scheme("https").host(domain).path(commentsEndpoint).build().toString();
    String postsUrl = UriComponentsBuilder.newInstance() //
      .scheme("https").host(domain).path(postsEndpoint).build().toString();
    String usersUrl = UriComponentsBuilder.newInstance() //
      .scheme("https").host(domain).path(usersEndpoint).build().toString();

    List<CommentDto> commentDtos = Arrays.asList(this.restTemplate.getForObject(commentsUrl, CommentDto[].class));
    List<PostDto> postDtos = Arrays.asList(this.restTemplate.getForObject(postsUrl, PostDto[].class));
    List<UserDto> userDtos = Arrays.asList(this.restTemplate.getForObject(usersUrl, UserDto[].class));

    if (commentDtos.size() == 0 || postDtos.size() == 0 || userDtos.size() == 0){
      throw BusinessException.of(SysCode.RESTTEMPLATE_ERROR);
    }

    // List<UserDTO.Post> postDTOs = postDtos.stream().map(e ->
    //   UserDTO.Post.builder().id(e.getId()).title(e.getTitle()).body(e.getBody()).build()) //
    //   .collect(Collectors.toList());

    // for (CommentDto c : commentDtos){
    //   for (UserDTO.Post p : postDTOs){
    //     if (c.getPostId() == p.getId()){
    //       p.getComments().add(UserDTO.Comment.builder() //
    //       .id(c.getId()).name(c.getName()).email(c.getEmail()).body(c.getBody()) //
    //       .build());
    //     }
    //   }
    // }

    userDtos.stream().forEach(e -> {
      UserDTO userDTO = UserDTO.builder().id(e.getId()).name(e.getName()) //
      .userName(e.getUsername()).email(e.getEmail()) //
      .phone(e.getPhone()).webSite(e.getWebsite()) //
      .address(UserDTO.Address.builder() //
          .street(e.getAddress().getStreet()).suite(e.getAddress().getSuite())//
          .city(e.getAddress().getCity()).zipcode(e.getAddress().getZipcode())
          .geo(UserDTO.Geo.builder()
              .lat(e.getAddress().getGeo().getLat()).lng(e.getAddress().getGeo().getLng()).build()
              ).build()) //
              .company(UserDTO.Company.builder() //
              .name(e.getCompany().getName()).catchPhrase(e.getCompany().getCatchPhrase()) //
                .bs(e.getCompany().getBs()).build()) //
                .posts(new ArrayList<>())
                .build();
      userDTOs.add(userDTO);
    });

    for (PostDto p : postDtos){
      for (UserDTO u : userDTOs){
        if (p.getUserId() == u.getId()){
          u.getPosts().add(UserDTO.Post.builder(). //
          id(p.getId()).title(p.getTitle()).body(p.getBody()).comments(new ArrayList<>()).build());
        }
      }
    }

    for (CommentDto c : commentDtos){
      for (UserDTO u : userDTOs){
       u.getPosts().stream().forEach(e ->{
        if (c.getId() == e.getId()){
          e.getComments().add(UserDTO.Comment.builder() //
          .id(c.getId()).name(c.getName()).email(c.getEmail()).body(c.getBody()).build());
        }
       }); 
      }
    }

    return userDTOs;
  }

  // @Override
  // public UserCommentDTO getUserComments(Long id){
  //   String usersUrl = UriComponentsBuilder.newInstance() //
  //     .scheme("https").host(domain).path(usersEndpoint).build().toString();
  //   String commentsUrl = UriComponentsBuilder.newInstance() //
  //     .scheme("https").host(domain).path(commentsEndpoint).build().toString();

  //   List<CommentDto> commentDtos = Arrays.asList(this.restTemplate.getForObject(commentsUrl, CommentDto[].class));
  //   List<UserDto> userDtos = Arrays.asList(this.restTemplate.getForObject(usersUrl, UserDto[].class));

  //   UserDto targetDto = null;

  //   for (UserDto u : userDtos){
  //     if (u.getId() == id){
  //       targetDto = u;
  //     }
  //   }

  //   List<UserCommentDTO.Comment> resultFound = new ArrayList<>();

  //   for (CommentDto c : commentDtos){
  //     if (c.getEmail() == targetDto.getEmail()){
  //       resultFound.add(UserCommentDTO.Comment.builder() //
  //         .name(c.getName()).email(c.getEmail()).body(c.getBody()).build());
  //     }
  //   }

  //   UserCommentDTO result = UserCommentDTO.builder() //
  //     .id(targetDto.getId()).username(targetDto.getUsername()).comments(resultFound).build();

  //   return result;
  // }

  @Override
  public UserCommentDTO getUserComments(Long id){
    String usersUrl = UriComponentsBuilder.newInstance() //
      .scheme("https").host(domain).path(usersEndpoint).build().toString();
    String postsUrl = UriComponentsBuilder.newInstance() //
      .scheme("https").host(domain).path(postsEndpoint).build().toString();
    String commentsUrl = UriComponentsBuilder.newInstance() //
      .scheme("https").host(domain).path(commentsEndpoint).build().toString();

    List<CommentDto> commentDtos = Arrays.asList(this.restTemplate.getForObject(commentsUrl, CommentDto[].class));
    List<PostDto> postDtos = Arrays.asList(this.restTemplate.getForObject(postsUrl, PostDto[].class));
    List<UserDto> userDtos = Arrays.asList(this.restTemplate.getForObject(usersUrl, UserDto[].class));

    UserCommentDTO resultUser = null;
    for (UserDto u : userDtos){
      if (u.getId() == id){
        resultUser = UserCommentDTO.builder() //
        .id(u.getId()).username(u.getUsername()).comments(new ArrayList<UserCommentDTO.Comment>()).build();
      }
    }
    if (resultUser == null){
      throw BusinessException.of(SysCode.USER_NOT_FOUND);
    }

    List<Long> targetPosts = new ArrayList<>();
    for (PostDto p : postDtos){
      if (p.getUserId() == id){
        targetPosts.add(p.getId());
      }
    }
    if (targetPosts.size() == 0){
      throw BusinessException.of(SysCode.POST_NOT_FOUND);
    }

    List<UserCommentDTO.Comment> foundComments = new ArrayList<>();
    for (CommentDto c : commentDtos){
      for (Long postId : targetPosts){
        if (c.getPostId() == postId){
          foundComments.add(UserCommentDTO.Comment.builder() //
            .name(c.getName()).email(c.getEmail()).body(c.getBody()).build());
        }
      }
    }
    if (foundComments.size() == 0){
      throw BusinessException.of(SysCode.COMMENT_NOT_FOUND);
    }

    resultUser.setComments(foundComments);
    return resultUser;
  }
}
