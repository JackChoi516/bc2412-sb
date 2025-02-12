package com.bootcamp.demo.demo_sb_customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Companies")
@Getter
@Setter
@Builder
public class CompanyEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "Name")
  private String name;
  @Column(name = "Catch_phrase")
  private String catchPhrase;
  @Column(name = "Bs")
  private String bs;
  @OneToOne
  @JoinColumn(name = "User_id")
  private UserEntity userEntity;
}
