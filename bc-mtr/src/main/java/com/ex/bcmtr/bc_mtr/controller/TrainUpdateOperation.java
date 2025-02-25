package com.ex.bcmtr.bc_mtr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.bcmtr.bc_mtr.dto.EarliestDTO;
import com.ex.bcmtr.bc_mtr.model.MtrDataDto;

public interface TrainUpdateOperation {
  @GetMapping(value = "/update")
  MtrDataDto getMtrDatadto(@RequestParam String line, @RequestParam String station);

  @GetMapping(value = "/earliest")
  EarliestDTO getEarliest(@RequestParam String line, @RequestParam String station);
}
