package com.fatihbozik.aviationroutefinder.rest.controller;

import com.fatihbozik.aviationroutefinder.domain.Route;
import com.fatihbozik.aviationroutefinder.mapper.TransportationMapper;
import com.fatihbozik.aviationroutefinder.rest.model.TransportationResponse;
import com.fatihbozik.aviationroutefinder.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
public class RouteController {
    private final RouteService routeService;
    private final TransportationMapper transportationMapper;

    @GetMapping("/{originCode}/{destinationCode}/{day}")
    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.AGENCY)")
    public List<List<TransportationResponse>> findRoutes(
            @PathVariable String originCode,
            @PathVariable String destinationCode,
            @PathVariable int day) {
        List<Route> routes = routeService.calculateRoutes(originCode, destinationCode, day);
        return routes.stream()
                .map(route -> transportationMapper.toResponses(route.steps()))
                .toList();
    }
}
