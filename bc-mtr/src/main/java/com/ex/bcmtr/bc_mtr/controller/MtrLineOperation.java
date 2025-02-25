package com.ex.bcmtr.bc_mtr.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ex.bcmtr.bc_mtr.entity.MtrLineEntity;
import com.ex.bcmtr.bc_mtr.model.MtrLine;
import com.ex.bcmtr.bc_mtr.model.MtrStation;

public interface MtrLineOperation {
  @GetMapping(value = "/lines")
  Map<MtrLine, List<MtrStation>> getAllLines();

  @GetMapping(value = "/line")
  List<MtrStation> getByLine(@RequestParam MtrLine line);
}
