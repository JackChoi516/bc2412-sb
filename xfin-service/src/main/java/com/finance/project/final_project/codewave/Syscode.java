package com.finance.project.final_project.codewave;

public enum Syscode {
  OK("000000", "Success."),
  STOCK_LISTS_NOT_FOUND("900001", "Stock list entities not found."),
  ENTITY_NOT_FOUND("900002", "Entities not found."),
  INVALID_INPUT("900003", "Invalid range."),
  INVALID_STOCK("999997", "Can not get stock data."),
  ILLEGAL_ARG("999998", "Illegal arguments."),
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
