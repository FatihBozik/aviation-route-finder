package com.fatihbozik.aviationroutefinder.repository;

import com.fatihbozik.aviationroutefinder.persistence.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    Optional<LocationEntity> findByCode(String code);
}
