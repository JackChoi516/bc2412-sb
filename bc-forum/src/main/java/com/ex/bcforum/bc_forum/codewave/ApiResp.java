package com.ex.bcforum.bc_forum.codewave;

import lombok.Getter;

@Getter
public class ApiResp{
  private String code;
  private String message;

  public static ApiResp of(SysCode sysCode){
    return new ApiResp(sysCode);
  }

  private ApiResp(SysCode sysCode){
    this.code = sysCode.getCode();
    this.message = sysCode.getMessage();
  }
}
