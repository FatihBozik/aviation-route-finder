package com.fatihbozik.aviationroutefinder.repository;

import com.fatihbozik.aviationroutefinder.model.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {

}
