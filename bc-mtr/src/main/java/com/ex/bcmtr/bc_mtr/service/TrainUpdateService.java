package com.ex.bcmtr.bc_mtr.service;

import com.ex.bcmtr.bc_mtr.model.MtrDataDto;

public interface TrainUpdateService {
  MtrDataDto getMtrDataDto(String line, String station);
}
