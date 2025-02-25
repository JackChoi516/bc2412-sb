package com.ex.bcmtr.bc_mtr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ex.bcmtr.bc_mtr.entity.MtrLineEntity;
import com.ex.bcmtr.bc_mtr.entity.MtrStationEntity;
import com.ex.bcmtr.bc_mtr.entity.mapper.EntityMapper;
import com.ex.bcmtr.bc_mtr.model.MtrLine;
import com.ex.bcmtr.bc_mtr.model.MtrStation;
import com.ex.bcmtr.bc_mtr.repository.MtrLineRepository;
import com.ex.bcmtr.bc_mtr.service.MtrLineDataService;

@Service
public class MtrLineDataServiceImpl implements MtrLineDataService{
  @Autowired
  private MtrLineRepository mtrLineRepository;
  @Autowired
  private EntityMapper entityMapper;

  @Override
  public void saveLine(){
    if (this.mtrLineRepository.count() == 0){
      for (MtrLine line : MtrLine.values()){
        this.mtrLineRepository.save(this.entityMapper.map(line));
      }
    }
  }

  @Override
  public Map<MtrLine, List<MtrStation>> getAllLines(){
    Map<MtrLine, List<MtrStation>> result = new HashMap<>();
    for (MtrLineEntity line :this.mtrLineRepository.findAll()){
      result.put(line.getLine(), line.getStations().stream().map(e -> e.getStation()).collect(Collectors.toList()));
    }
    return result;
  }

  @Override
  public List<MtrStation> getByLine(MtrLine line){
    for (MtrLineEntity lineE : this.mtrLineRepository.findAll()){
      if (lineE.getLine() == line){
        return lineE.getStations().stream().map(e -> e.getStation()).collect(Collectors.toList());
      }
    }
    return null;
  }

}
