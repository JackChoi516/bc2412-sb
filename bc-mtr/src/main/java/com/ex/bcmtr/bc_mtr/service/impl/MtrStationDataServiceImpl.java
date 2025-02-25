package com.ex.bcmtr.bc_mtr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ex.bcmtr.bc_mtr.entity.MtrStationEntity;
import com.ex.bcmtr.bc_mtr.entity.mapper.EntityMapper;
import com.ex.bcmtr.bc_mtr.model.MtrLine;
import com.ex.bcmtr.bc_mtr.model.MtrStation;
import com.ex.bcmtr.bc_mtr.repository.MtrLineRepository;
import com.ex.bcmtr.bc_mtr.repository.MtrStationRepository;
import com.ex.bcmtr.bc_mtr.service.MtrStationDataService;

@Service
public class MtrStationDataServiceImpl implements MtrStationDataService{
  @Autowired
  private MtrStationRepository mtrStationRepository;
  @Autowired
  private MtrLineRepository mtrLineRepository;
  @Autowired
  private EntityMapper entityMapper;

  @Override
  public void saveStation(){
    System.out.println("-----------------------------------------------------------");
    if (this.mtrStationRepository.count() == 0){
      for (MtrStation s : MtrStation.values()){
        this.mtrStationRepository.save(this.entityMapper.map(s));  
      }
    }
  }

  @Override
  public void setLine(){
    if (
      this.mtrStationRepository.count() > 1
      && this.mtrLineRepository.count() > 1
      ){
      System.out.println("*******************set Line*********************");
    for (MtrStationEntity s : this.mtrStationRepository.findAll()){
      if (s.getStation().getCode() >= 1 && s.getStation().getCode() <= 5){
        s.getLines().add(this.mtrLineRepository.findByLine(MtrLine.AEL));
      }
      if (s.getStation().getCode() <= 3 || (s.getStation().getCode() >= 6 && s.getStation().getCode() <= 10)){
        s.getLines().add(this.mtrLineRepository.findByLine(MtrLine.TCL));
      }
      if ((s.getStation().getCode() >= 11 && s.getStation().getCode() <= 28) || s.getStation().getCode() == 7 
      || (s.getStation().getCode() >= 29 && s.getStation().getCode() <= 36)){
        s.getLines().add(this.mtrLineRepository.findByLine(MtrLine.TML));
      }
      if (s.getStation().getCode() >= 37 && s.getStation().getCode() <= 44){
        s.getLines().add(this.mtrLineRepository.findByLine(MtrLine.TKL));
      }
      if ((s.getStation().getCode() >= 45 && s.getStation().getCode() <= 58) || s.getStation().getCode() == 26 //
      || s.getStation().getCode() == 19){
        s.getLines().add(this.mtrLineRepository.findByLine(MtrLine.EAL));
      }
      if (s.getStation().getCode() == 45 || (s.getStation().getCode() >= 59 && s.getStation().getCode() <= 62)){
        s.getLines().add(this.mtrLineRepository.findByLine(MtrLine.SIL));
      }
      if (s.getStation().getCode() == 45 || (s.getStation().getCode() >= 63 && s.getStation().getCode() <=74) 
      || s.getStation().getCode() == 29 || s.getStation().getCode() == 8 || s.getStation().getCode() == 97){
        s.getLines().add(this.mtrLineRepository.findByLine(MtrLine.TWL));
      }
      if ((s.getStation().getCode() >= 75 && s.getStation().getCode() <= 87) || s.getStation().getCode() == 63 
      || s.getStation().getCode() == 45 || s.getStation().getCode() == 37 || s.getStation().getCode() == 38){
        s.getLines().add(this.mtrLineRepository.findByLine(MtrLine.ISL));
      }
      if ((s.getStation().getCode() >= 88 && s.getStation().getCode() <= 96) //
      || s.getStation().getCode() == 21 || s.getStation().getCode() == 25 || s.getStation().getCode() == 39
      || s.getStation().getCode() ==  40 || s.getStation().getCode() ==  48 || s.getStation().getCode() == 66
      || s.getStation().getCode() ==  67 || s.getStation().getCode() == 68){
        s.getLines().add(this.mtrLineRepository.findByLine(MtrLine.KTL));
      }
    }
  }
  }
}
