package com.ex.bcforum.bc_forum.codewave;

public class BusinessException extends RuntimeException{
  private SysCode sysCode;
  // private String code;
  // private String message;

  private BusinessException(SysCode sysCode){
    super(sysCode.getMessage());
    this.sysCode = sysCode;
    // this.code = sysCode.getCode();
    // this.message = sysCode.getMessage();
  }

  public static BusinessException of(SysCode sysCode){
    return new BusinessException(sysCode);
  }

  public SysCode getSysCode(){
    return this.sysCode;
  }

  // public String getCode(){
  // return this.code;
  // }

  // public String getMessage(){
  //   return this.message;
  // }
}
