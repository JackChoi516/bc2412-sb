package com.ex.bcmtr.bc_mtr.dto.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ex.bcmtr.bc_mtr.dto.EarliestDTO;
import com.ex.bcmtr.bc_mtr.dto.LineSignalDTO;
import com.ex.bcmtr.bc_mtr.model.MtrDataDto;

@Component
public class DTOMapper {
  public EarliestDTO.TrainData map(String direction, MtrDataDto.TrainData trainData){
    return EarliestDTO.TrainData.builder().Direction(direction) //
      .arrivalTime(trainData.getTime()) //
      .destination(trainData.getDest().toString()) //
      .build();
  }


}
