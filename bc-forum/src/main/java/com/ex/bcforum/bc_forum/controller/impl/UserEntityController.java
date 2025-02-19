package com.ex.bcforum.bc_forum.controller.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.internal.build.AllowNonPortable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ex.bcforum.bc_forum.codewave.ApiResp;
import com.ex.bcforum.bc_forum.codewave.SysCode;
import com.ex.bcforum.bc_forum.controller.UserEntityOperation;
import com.ex.bcforum.bc_forum.entity.UserEntity;
import com.ex.bcforum.bc_forum.service.UserEntityService;
@RestController
public class UserEntityController implements UserEntityOperation{
  @Autowired
  private UserEntityService userEntityService;

  @Override
  public ApiResp<List<UserEntity>> getUserEntities(){
    return ApiResp.<List<UserEntity>>builder(). //
      sysCode(SysCode.OK).data(this.userEntityService.getUserEntities()).build();
  }

  @Override
  public ApiResp<Optional<UserEntity>> findById(Long id){
    Optional<UserEntity> result = this.userEntityService.findById(id);
    if (result.isPresent()){
      return ApiResp.<Optional<UserEntity>>builder() //
        .sysCode(SysCode.OK).data(result).build();
    }
    return ApiResp.<Optional<UserEntity>>builder() //
      .sysCode(SysCode.USER_NOT_FOUND).data(result).build();
  }

  @Override
  public ApiResp<UserEntity> putById(UserEntity userEntity){
    UserEntity result = this.userEntityService.putById(userEntity);
    return ApiResp.<UserEntity>builder() //
      .sysCode(SysCode.OK).data(result).build();
  }
}
