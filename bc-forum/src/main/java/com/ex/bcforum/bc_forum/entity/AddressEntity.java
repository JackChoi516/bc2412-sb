package com.ex.bcforum.bc_forum.entity;

import java.util.List;

import com.ex.bcforum.bc_forum.dto.UserDTO.Geo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Addresses")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String street;
  private String suite;
  private String city;
  private String zipcode;
  @ManyToOne
  @JoinColumn(name = "Geo_id")
  @Setter
  private GeoEntity geoEntity;
  @OneToMany(mappedBy = "addressEntity")
  @Setter
  @Getter
  @JsonIgnore
  private List<UserEntity> userEntity;
}
