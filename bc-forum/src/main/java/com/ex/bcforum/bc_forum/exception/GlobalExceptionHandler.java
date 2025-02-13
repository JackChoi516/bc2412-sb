package com.ex.bcforum.bc_forum.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ex.bcforum.bc_forum.codewave.ApiResp;
import com.ex.bcforum.bc_forum.codewave.BusinessException;
import com.ex.bcforum.bc_forum.codewave.SysCode;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = BusinessException.class)
  public ApiResp handleBussinessException(BusinessException e){
    return ApiResp.of(e.getSysCode());
  }

  @ExceptionHandler(value = IllegalArgumentException.class)
  public ApiResp handleIllegalArgException(IllegalArgumentException e){
    return ApiResp.of(SysCode.INVALID_INPUT);
  }

}
