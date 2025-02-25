package com.ex.bcmtr.bc_mtr.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EarliestDTO {
  private String curr_time;
  private String sys_time;
  private String currentStation;
  private List<TrainData> trains;

  @Builder
  @Getter
  public static class TrainData{
    private String destination;
    private String arrivalTime;
    private String Direction;
  }
}
