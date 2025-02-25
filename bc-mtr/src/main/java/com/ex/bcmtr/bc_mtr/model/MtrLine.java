package com.ex.bcmtr.bc_mtr.model;

import lombok.Getter;

@Getter
public enum MtrLine {
  AEL("Airport Express"),
  TCL("Tung Chung Line"),
  TML("Tuen Ma Line"),
  TKL("Tseung Kwan O Line"),
  EAL("East Rail Line"),
  SIL("South Island Line"),
  TWL("Tsuen Wan Line"),
  ISL("Island Line"),
  KTL("Kwun Tong Line"),
  ;

  private String description;

  private MtrLine (String description){
    this.description = description;
  }
}
