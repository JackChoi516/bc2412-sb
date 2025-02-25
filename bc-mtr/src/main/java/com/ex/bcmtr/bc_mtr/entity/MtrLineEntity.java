package com.ex.bcmtr.bc_mtr.entity;

import java.util.List;
import com.ex.bcmtr.bc_mtr.model.MtrLine;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Lines")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MtrLineEntity {
  @Id
  @Enumerated(EnumType.STRING)
  private MtrLine line;
  @ManyToMany(mappedBy = "lines")
  private List<MtrStationEntity> stations;
}
