package com.ex.bcmtr.bc_mtr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ex.bcmtr.bc_mtr.repository.MtrStationRepository;
import com.ex.bcmtr.bc_mtr.service.MtrLineDataService;
import com.ex.bcmtr.bc_mtr.service.MtrStationDataService;

@Component
public class PreServerStartConfig implements CommandLineRunner {
  @Autowired
  private MtrLineDataService mtrLineDataService;
  @Autowired
  private MtrStationDataService mtrStationDataService;
  @Autowired
  private MtrStationRepository mtrStationRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception{
    if (this.mtrStationRepository.count() == 0){
      this.mtrLineDataService.saveLine();
      this.mtrStationDataService.saveStation();
      this.mtrStationDataService.setLine();
    }
    System.out.println("Data saved.");
  }
}
