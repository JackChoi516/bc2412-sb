package com.ex.bcmtr.bc_mtr.service;

import java.util.List;

import com.ex.bcmtr.bc_mtr.dto.EarliestDTO;
import com.ex.bcmtr.bc_mtr.dto.LineSignalDTO;
import com.ex.bcmtr.bc_mtr.model.MtrDataDto;

public interface TrainUpdateService {
  MtrDataDto getMtrDataDto(String line, String station);
  EarliestDTO getEarliest(String line, String station);
  LineSignalDTO getLineSignal(String line);
  List<LineSignalDTO> getAllLineSignals();
}
