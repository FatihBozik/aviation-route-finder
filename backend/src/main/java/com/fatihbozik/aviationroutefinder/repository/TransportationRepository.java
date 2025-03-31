package com.fatihbozik.aviationroutefinder.repository;

import com.fatihbozik.aviationroutefinder.persistence.TransportationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationRepository extends JpaRepository<TransportationEntity, Long> {
}
