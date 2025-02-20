package com.ex.bcforum.bc_forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.bcforum.bc_forum.entity.AddressEntity;
import java.util.List;
import com.ex.bcforum.bc_forum.entity.GeoEntity;


public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
  AddressEntity findByStreetAndSuiteAndCityAndZipcode //
    (String street, String suite, String city, String zipcode);
}
