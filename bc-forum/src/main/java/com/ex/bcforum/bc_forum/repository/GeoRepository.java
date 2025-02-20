package com.ex.bcforum.bc_forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.bcforum.bc_forum.entity.GeoEntity;
import java.util.List;


public interface GeoRepository extends JpaRepository<GeoEntity, Long>{
  GeoEntity findByLatitudeAndLongitude(Double latitude, Double longitude);
}
