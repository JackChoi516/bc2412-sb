package com.ex.bcmtr.bc_mtr.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ex.bcmtr.bc_mtr.controller.TrainUpdateOperation;
import com.ex.bcmtr.bc_mtr.dto.EarliestDTO;
import com.ex.bcmtr.bc_mtr.dto.LineSignalDTO;
import com.ex.bcmtr.bc_mtr.dto.mapper.DTOMapper;
import com.ex.bcmtr.bc_mtr.model.MtrDataDto;
import com.ex.bcmtr.bc_mtr.service.TrainUpdateService;

@RestController
public class TrainUpdateController implements TrainUpdateOperation{
  @Autowired
  private TrainUpdateService trainUpdateService;

  @Override
  public MtrDataDto getMtrDatadto(String line, String station){
    return this.trainUpdateService.getMtrDataDto(line, station);
  }

  @Override
  public EarliestDTO getEarliest(String line, String station){
    return this.trainUpdateService.getEarliest(line, station);
  }

  @Override
  public LineSignalDTO getLineSignal(String line){
    return this.trainUpdateService.getLineSignal(line);
  }

  @Override
  public List<LineSignalDTO> getAllSignals(){
    return this.trainUpdateService.getAllLineSignals();
  }
}
