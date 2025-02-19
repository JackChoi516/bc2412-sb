package com.ex.bcforum.bc_forum.codewave;

import java.lang.module.ModuleDescriptor.Builder;
import java.util.List;

import lombok.Getter;

@Getter
public class ApiResp<T>{
  private String code;
  private String message;
  private T data;

  public static <T>Builder<T> builder(){
    return new Builder<>();
  }

  public ApiResp(){

  }
  
  public ApiResp(Builder<T> builder){
    this.code = builder.code;
    this.message = builder.message;
    this.data = builder.data;
  }

  public static class Builder<T>{
    private String code;
    private String message;
    private T data;

    public Builder<T> sysCode(SysCode sysCode){
      this.code = sysCode.getCode();
      this.message = sysCode.getMessage();
      return this;
    }

    public Builder<T> data(T data){
      this.data = data;
      return this;
    }

    public ApiResp<T> build(){
      return new ApiResp<>(this);
    }

  }
}
