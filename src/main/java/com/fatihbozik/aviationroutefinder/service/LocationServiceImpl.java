package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.dto.Location;
import com.fatihbozik.aviationroutefinder.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Location> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(Location::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createLocation(Location location) {
        // locationRepository.save(location);

    }

    @Override
    @Transactional
    public void deletePet(Location location) {

    }
}
