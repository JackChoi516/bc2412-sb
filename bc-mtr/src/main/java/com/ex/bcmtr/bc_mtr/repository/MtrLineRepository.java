package com.ex.bcmtr.bc_mtr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ex.bcmtr.bc_mtr.entity.MtrLineEntity;
import com.ex.bcmtr.bc_mtr.model.MtrLine;
import java.util.List;

@Repository
public interface MtrLineRepository extends JpaRepository<MtrLineEntity, MtrLine>{
  MtrLineEntity findByLine(MtrLine line);
}
