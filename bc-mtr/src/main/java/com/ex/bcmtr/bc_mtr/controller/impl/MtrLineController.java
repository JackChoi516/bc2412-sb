package com.ex.bcmtr.bc_mtr.controller.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ex.bcmtr.bc_mtr.controller.MtrLineOperation;
import com.ex.bcmtr.bc_mtr.entity.MtrLineEntity;
import com.ex.bcmtr.bc_mtr.model.MtrLine;
import com.ex.bcmtr.bc_mtr.model.MtrStation;
import com.ex.bcmtr.bc_mtr.service.MtrLineDataService;

@RestController
public class MtrLineController implements MtrLineOperation{
  @Autowired
  private MtrLineDataService mtrLineDataService;

  @Override
  public Map<MtrLine, List<MtrStation>> getAllLines(){
    return this.mtrLineDataService.getAllLines();
  }
   
  @Override
  public List<MtrStation> getByLine(MtrLine line){
    return this.mtrLineDataService.getByLine(line);
  }
}
