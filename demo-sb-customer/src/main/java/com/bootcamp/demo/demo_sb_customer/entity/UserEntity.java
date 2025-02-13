package com.bootcamp.demo.demo_sb_customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "Name")
  private String name;
  @Column(name = "User_name")
  private String username;
  @Column(name = "Email")
  private String email;
  @ManyToOne
  @JoinColumn(name = "Address")
  private AddressEntity addressEntity;
  @Column(name = "Phone")
  private String phone;
  @Column(name = "Website")
  private String website;
  @ManyToOne
  @JoinColumn(name = "Company")
  private CompanyEntity companyEntity;
}
