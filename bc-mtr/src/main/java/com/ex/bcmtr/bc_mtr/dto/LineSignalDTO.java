package com.ex.bcmtr.bc_mtr.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class LineSignalDTO {
  private String line;
  private String signal;
  private List<String> delayedStation;
  private String curr_time;
  private String sys_time;
}
