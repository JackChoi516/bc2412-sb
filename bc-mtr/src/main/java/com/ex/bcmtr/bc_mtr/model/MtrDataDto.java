package com.ex.bcmtr.bc_mtr.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MtrDataDto {
  private Integer status;
  private String message;
  @JsonFormat(pattern = "yyyy-MM-dd H:mm:ss")
  private LocalDateTime sys_time;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime curr_time;
  private Map<String, LineStation> data;
  private String isdelay;

  @Getter
  @Setter
  public static class LineStation{
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime curr_time;
    @JsonFormat(pattern = "yyyy-MM-dd H:mm:ss")
    private LocalDateTime sys_time;
    @JsonProperty("DOWN")
    private List<TrainData> DOWN;
    @JsonProperty("UP")
    private List<TrainData> UP;
  }

  @Getter
  @Setter
  public static class TrainData{
    private String seq;
    private MtrStation dest;
    private String plat;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    private String ttnt;
    private String valid;
    private String source;
  }
}
