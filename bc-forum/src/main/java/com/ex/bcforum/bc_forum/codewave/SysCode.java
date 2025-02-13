package com.ex.bcforum.bc_forum.codewave;

public enum SysCode {
  OK("000000", "Success"),
  USER_NOT_FOUND("000001", "User not found."),
  POST_NOT_FOUND("000002", "Post not found."),
  COMMENT_NOT_FOUND("000003", "Comment not found."),
  INVALID_INPUT("000004", "Invalid input."),
  RESTTEMPLATE_ERROR("000005", "RestTemplate Error - jsonplaceholder"),
  ;

  private String code;
  private String message;

  private SysCode(String code, String message){
    this.code = code;
    this.message = message;
  }

  public String getCode(){
    return this.code;
  }

  public String getMessage(){
    return this.message;
  }
}
