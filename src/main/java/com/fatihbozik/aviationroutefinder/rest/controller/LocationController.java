package com.fatihbozik.aviationroutefinder.rest.controller;

import com.fatihbozik.aviationroutefinder.dto.Location;
import com.fatihbozik.aviationroutefinder.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.AGENCY)")
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }
}
