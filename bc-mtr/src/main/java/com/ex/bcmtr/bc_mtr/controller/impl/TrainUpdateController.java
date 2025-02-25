package com.ex.bcmtr.bc_mtr.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ex.bcmtr.bc_mtr.controller.TrainUpdateOperation;
import com.ex.bcmtr.bc_mtr.dto.EarliestDTO;
import com.ex.bcmtr.bc_mtr.dto.mapper.DTOMapper;
import com.ex.bcmtr.bc_mtr.model.MtrDataDto;
import com.ex.bcmtr.bc_mtr.service.TrainUpdateService;

@RestController
public class TrainUpdateController implements TrainUpdateOperation{
  @Autowired
  private TrainUpdateService trainUpdateService;
  @Autowired
  private DTOMapper dtoMapper;

  @Override
  public MtrDataDto getMtrDatadto(String line, String station){
    return this.trainUpdateService.getMtrDataDto(line, station);
  }

  @Override
  public EarliestDTO getEarliest(String line, String station){
    return this.dtoMapper.map(line, station, this.getMtrDatadto(line, station));
  }
}
