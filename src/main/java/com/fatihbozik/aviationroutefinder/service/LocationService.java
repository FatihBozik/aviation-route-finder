package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.dto.Location;

import java.util.List;

public interface LocationService {
    List<Location> getAllLocations();

    void createLocation(Location location);

    void deletePet(Location location);
}
