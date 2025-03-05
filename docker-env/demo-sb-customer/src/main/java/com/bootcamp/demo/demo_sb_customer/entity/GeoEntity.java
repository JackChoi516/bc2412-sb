package com.bootcamp.demo.demo_sb_customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Geos")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeoEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "Latitude")
  private Double latitude;
  @Column(name = "Longitude")
  private Double longitude;
  @OneToOne
  @JoinColumn(name = "address_id")
  @Setter
  private AddressEntity addressEntity;
}
