package com.ex.bcmtr.bc_mtr.entity.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.ex.bcmtr.bc_mtr.entity.MtrLineEntity;
import com.ex.bcmtr.bc_mtr.entity.MtrStationEntity;
import com.ex.bcmtr.bc_mtr.model.MtrLine;
import com.ex.bcmtr.bc_mtr.model.MtrStation;

@Component
public class EntityMapper {
  public MtrStationEntity map(MtrStation mtrStation){
    return MtrStationEntity.builder().station(mtrStation).lines(new ArrayList<>()).build();
  }

  public MtrLineEntity map(MtrLine mtrLine){
    return MtrLineEntity.builder().line(mtrLine).stations(new ArrayList<>()).build();
  }
}
