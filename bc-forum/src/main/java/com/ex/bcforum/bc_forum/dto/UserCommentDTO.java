package com.ex.bcforum.bc_forum.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Builder
@Getter
public class UserCommentDTO {
  private Long id;
  private String username;
  @Setter
  private List<Comment> comments;

  @Builder
  @Getter
  public static class Comment{
    private String name;
    private String email;
    private String body;
  }
}
