package com.bootcamp.demo.demo_sb_customer.codewave;

public enum Syscode {
  OK("000000", "Success."),
  ID_NOT_FOUND("900001", "ID not found."),

  // Pre-handled Runtime Exception
  RTE_NPE("999999", "Null Pointer Exception."),;

  private final String code;
  private final String message;

  private Syscode(String code, String message){
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
