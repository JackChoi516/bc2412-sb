package com.ex.bcmtr.bc_mtr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ex.bcmtr.bc_mtr.entity.MtrStationEntity;
import com.ex.bcmtr.bc_mtr.model.MtrStation;
import java.util.List;

@Repository
public interface MtrStationRepository extends JpaRepository<MtrStationEntity, MtrStation>{
  MtrStationEntity findByStation(MtrStation station);
}
