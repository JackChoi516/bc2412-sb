package com.ex.bcmtr.bc_mtr.dto.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ex.bcmtr.bc_mtr.dto.EarliestDTO;
import com.ex.bcmtr.bc_mtr.model.MtrDataDto;

@Component
public class DTOMapper {
  public EarliestDTO map(String line, String station, MtrDataDto mtrDataDto){
    List<EarliestDTO.TrainData> trainData = new ArrayList<>();
    Set<String> destUp = new HashSet<>();
    Set<String> destDn = new HashSet<>();
    mtrDataDto.getData().values().stream().forEach(e -> {
      e.getUP().stream().forEach(u -> {
        if (Integer.valueOf(u.getSeq()) == 1){
          trainData.add(this.map("UP", u));
          destUp.add(u.getDest().toString());
        }
      });
      e.getUP().stream().forEach(u2 -> {
        if (destUp.add(u2.getDest().toString())){
          trainData.add(this.map("UP", u2));
        }
      });

      e.getDOWN().stream().forEach(d -> {
        if (Integer.valueOf(d.getSeq()) == 1){
          trainData.add(this.map("DOWN", d));
          destDn.add(d.getDest().toString());
        }
      });
      e.getDOWN().stream().forEach(d2 -> {
        if (destDn.add(d2.getDest().toString())){
          trainData.add(this.map("DOWN", d2));
        }
      });
    });
    
    return EarliestDTO.builder().curr_time(mtrDataDto.getCurr_time().toString()) //
      .sys_time(mtrDataDto.getSys_time().toString()) //
      .currentStation(station).trains(trainData).build();
  }

  public EarliestDTO.TrainData map(String direction, MtrDataDto.TrainData trainData){
    return EarliestDTO.TrainData.builder().Direction(direction) //
      .arrivalTime(trainData.getTime().toString()) //
      .destination(trainData.getDest().toString()) //
      .build();
  }
}
