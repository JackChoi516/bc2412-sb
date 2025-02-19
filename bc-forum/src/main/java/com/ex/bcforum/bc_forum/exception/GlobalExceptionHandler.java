package com.ex.bcforum.bc_forum.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import com.ex.bcforum.bc_forum.codewave.ApiResp;
import com.ex.bcforum.bc_forum.codewave.BusinessException;
import com.ex.bcforum.bc_forum.codewave.SysCode;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = BusinessException.class)
  public ApiResp<Void> handleBussinessException(BusinessException e){
    return ApiResp.<Void>builder().sysCode(e.getSysCode()).build();
  }

  @ExceptionHandler(value = IllegalArgumentException.class)
  public ApiResp<Void> handleIllegalArgException(IllegalArgumentException e){
    return ApiResp.<Void>builder().sysCode(SysCode.INVALID_INPUT).build();
  }

  @ExceptionHandler(value = HttpClientErrorException.class)
  public ApiResp<Void> handleHttpClientErrorException(HttpClientErrorException e){
    return ApiResp.<Void>builder().sysCode(SysCode.RESTTEMPLATE_ERROR).build();
  }

}
