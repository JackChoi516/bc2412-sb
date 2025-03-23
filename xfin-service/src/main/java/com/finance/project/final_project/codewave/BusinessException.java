package com.finance.project.final_project.codewave;

public class BusinessException extends RuntimeException {
  private Syscode syscode;

  public static BusinessException of (Syscode syscode){
    return new BusinessException(syscode);
  }

  private BusinessException(Syscode syscode){
    super(syscode.getMessage());
    this.syscode = syscode;
  }

  public Syscode getSyscode(){
    return this.syscode;
  }
}
