package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.domain.Route;
import com.fatihbozik.aviationroutefinder.domain.Transportation;
import com.fatihbozik.aviationroutefinder.mapper.TransportationMapper;
import com.fatihbozik.aviationroutefinder.persistence.LocationEntity;
import com.fatihbozik.aviationroutefinder.persistence.TransportationEntity;
import com.fatihbozik.aviationroutefinder.persistence.TransportationType;
import com.fatihbozik.aviationroutefinder.repository.LocationRepository;
import com.fatihbozik.aviationroutefinder.repository.TransportationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final LocationRepository locationRepository;
    private final TransportationRepository transportationRepository;
    private final TransportationMapper transportationMapper;

    @Override
    public List<Route> calculateRoutes(String originCode, String destinationCode, int day) {
        final LocationEntity origin = locationRepository.findByCode(originCode)
                .orElseThrow(() -> new EntityNotFoundException("Origin Location not found: " + originCode));
        final LocationEntity destination = locationRepository.findByCode(destinationCode)
                .orElseThrow(() -> new EntityNotFoundException("Destination Location not found" + destinationCode));

        List<TransportationEntity> allTransportations = transportationRepository.findAll();

        Map<LocationEntity, List<TransportationEntity>> graph = buildGraph(allTransportations, day);

        List<Route> result = new ArrayList<>();
        Set<LocationEntity> visited = new HashSet<>();

        dfs(origin, destination, graph, new ArrayList<>(), visited, result);

        return result;
    }

    private Map<LocationEntity, List<TransportationEntity>> buildGraph(List<TransportationEntity> transportations, int day) {
        return transportations.stream()
                .filter(t -> t.getOperatingDays().contains(day))
                .collect(Collectors.groupingBy(TransportationEntity::getOrigin));
    }

    private void dfs(LocationEntity current,
                     LocationEntity target,
                     Map<LocationEntity, List<TransportationEntity>> graph,
                     List<TransportationEntity> path,
                     Set<LocationEntity> visited,
                     List<Route> result) {

        // Check if we reached the target
        if (current.equals(target)) {
            // Only validate the route if we've reached the destination
            if (isValidRoute(path)) {
                result.add(new Route(new ArrayList<>(mapToDomainList(path))));
            }
            return;
        }

        // Early path length check
        if (path.size() >= 3) {
            return; // Limit exceeded, backtrack
        }

        // Early flight count check
        long flightCount = path.stream()
                .filter(t -> t.getType() == TransportationType.FLIGHT)
                .count();
        if (flightCount > 1) {
            return; // More than one flight, backtrack
        }

        visited.add(current);

        List<TransportationEntity> options = graph.getOrDefault(current, Collections.emptyList());
        for (TransportationEntity transportation : options) {
            if (!visited.contains(transportation.getDestination())) {
                path.add(transportation);
                dfs(transportation.getDestination(), target, graph, path, visited, result);
                path.removeLast();
            }
        }

        visited.remove(current);
    }

    // Separate method for route validation
    private boolean isValidRoute(List<TransportationEntity> path) {
        // Check path length
        if (path.size() > 3) {
            return false;
        }

        // Exactly one flight check
        long flightCount = path.stream()
                .filter(t -> t.getType() == TransportationType.FLIGHT)
                .count();
        if (flightCount != 1) {
            return false; // Must have exactly one flight
        }

        // Find flight index
        int flightIndex = -1;
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).getType() == TransportationType.FLIGHT) {
                flightIndex = i;
                break;
            }
        }

        // Uçuş öncesi en fazla bir taşıma olmalı
        if (flightIndex > 1) {
            return false;
        }

        // Uçuş sonrası en fazla bir taşıma olmalı
        if ((path.size() - flightIndex - 1) > 1) {
            return false;
        }

        return true;
    }

    private List<Transportation> mapToDomainList(List<TransportationEntity> entities) {
        return transportationMapper.toDomains(entities);
    }
}
