package com.ex.bcmtr.bc_mtr.entity;

import java.util.List;

import javax.sound.sampled.Line;

import com.ex.bcmtr.bc_mtr.model.MtrLine;
import com.ex.bcmtr.bc_mtr.model.MtrStation;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Stations")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MtrStationEntity {
  @Id
  @Enumerated(EnumType.STRING)
  private MtrStation station;
  @ManyToMany
  @JoinTable(
    name = "Stations_lines",
    joinColumns = @JoinColumn(name = "station_id"),
    inverseJoinColumns = @JoinColumn(name = "line_id")
  )
  @JsonIgnore
  private List<MtrLineEntity> lines;
}
