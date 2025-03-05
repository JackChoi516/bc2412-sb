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
@Table(name = "Addresses")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "Street")
  private String street;
  @Column(name = "Suite")
  private String suite;
  @Column(name = "City")
  private String city;
  @Column(name = "Zipcode")
  private String zipcode;
  @OneToOne
  @JoinColumn(name = "User_id")
  @Setter
  private UserEntity userEntity;

}
