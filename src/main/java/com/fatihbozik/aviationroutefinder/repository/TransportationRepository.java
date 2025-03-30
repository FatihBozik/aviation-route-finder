package com.fatihbozik.aviationroutefinder.repository;

import com.fatihbozik.aviationroutefinder.persistence.TransportationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationRepository extends JpaRepository<TransportationEntity, Long> {

//    @Query(value = "SELECT * FROM transportations t WHERE t.origin_location_id = :originId AND :day = ANY (t.operating_days)",
//            nativeQuery = true)
//    List<TransportationEntity> findByOriginAndOperatingDaysContaining(LocationEntity origin, Integer day);
}
