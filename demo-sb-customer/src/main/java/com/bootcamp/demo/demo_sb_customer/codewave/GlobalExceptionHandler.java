package com.bootcamp.demo.demo_sb_customer.codewave;

import org.springframework.web.bind.annotation.ExceptionHandler;

// @RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = BusinessException.class)
  public ApiResp<Void> handleError(BusinessException e){
    return ApiResp.<Void>builder() //
        .syscode(e.getSyscode()) //
        .build();
  }

  @ExceptionHandler(value = NullPointerException.class)
  public ApiResp<Void> handleNullPointerException(){
    return ApiResp.<Void>builder() //
        .syscode(Syscode.RTE_NPE) //
        .build();
  }
}
