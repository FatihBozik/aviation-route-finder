package com.fatihbozik.aviationroutefinder.rest.controller;

import com.fatihbozik.aviationroutefinder.domain.Location;
import com.fatihbozik.aviationroutefinder.mapper.LocationMapper;
import com.fatihbozik.aviationroutefinder.rest.model.LocationRequest;
import com.fatihbozik.aviationroutefinder.rest.model.LocationResponse;
import com.fatihbozik.aviationroutefinder.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;
    private final LocationMapper locationMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.AGENCY)")
    public List<LocationResponse> getAllLocations() {
        final List<Location> allLocations = locationService.getAll();
        return locationMapper.toResponses(allLocations);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole(@roles.ADMIN)")
    public LocationResponse createLocation(@RequestBody @Valid LocationRequest request) {
        final Location location = locationMapper.toDomain(request);
        final Location createdLocation = locationService.create(location);
        return locationMapper.toResponse(createdLocation);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole(@roles.ADMIN)")
    public LocationResponse getLocation(@PathVariable("id") Long locationId) {
        final Location location = locationService.get(locationId);
        return locationMapper.toResponse(location);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole(@roles.ADMIN)")
    public LocationResponse updateLocation(@PathVariable("id") Long locationId, @RequestBody LocationRequest request) {
        Location location = locationMapper.toDomain(request);
        Location updatedLocation = locationService.update(locationId, location);
        return locationMapper.toResponse(updatedLocation);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole(@roles.ADMIN)")
    public void deleteLocation(@PathVariable("id") Long locationId) {
        locationService.delete(locationId);
    }
}
