package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.domain.Location;

import java.util.List;

public interface LocationService {
    List<Location> getAll();

    Location create(Location location);

    void delete(Long locationId);

    Location update(Long id, Location location);

    Location get(Long id);
}
