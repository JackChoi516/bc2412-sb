package com.ex.bcmtr.bc_mtr.service;

import java.util.List;
import java.util.Map;

import com.ex.bcmtr.bc_mtr.entity.MtrLineEntity;
import com.ex.bcmtr.bc_mtr.entity.MtrStationEntity;
import com.ex.bcmtr.bc_mtr.model.MtrLine;
import com.ex.bcmtr.bc_mtr.model.MtrStation;

public interface MtrLineDataService {
  void saveLine();
  Map<MtrLine, List<MtrStation>> getAllLines();
  List<MtrStation> getByLine(MtrLine line);
}
