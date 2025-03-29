package com.fatihbozik.aviationroutefinder.repository;

import com.fatihbozik.aviationroutefinder.model.LocationEntity;
import com.fatihbozik.aviationroutefinder.model.TransportationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportationRepository extends JpaRepository<TransportationEntity, Integer> {
    List<TransportationEntity> findByOriginAndOperatingDaysContaining(LocationEntity origin, Integer day);
}
