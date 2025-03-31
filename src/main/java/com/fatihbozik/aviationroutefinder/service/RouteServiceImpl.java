package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.domain.Route;
import com.fatihbozik.aviationroutefinder.domain.Transportation;
import com.fatihbozik.aviationroutefinder.mapper.TransportationMapper;
import com.fatihbozik.aviationroutefinder.persistence.LocationEntity;
import com.fatihbozik.aviationroutefinder.persistence.TransportationEntity;
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

        if (current.equals(target)) {
            result.add(new Route(new ArrayList<>(mapToDomainList(path))));
            return;
        }

        visited.add(current);

        List<TransportationEntity> options = graph.getOrDefault(current, Collections.emptyList());
        for (TransportationEntity t : options) {
            if (!visited.contains(t.getDestination())) {
                path.add(t);
                dfs(t.getDestination(), target, graph, path, visited, result);
                path.removeLast();
            }
        }

        visited.remove(current);
    }

    private List<Transportation> mapToDomainList(List<TransportationEntity> entities) {
        return transportationMapper.toDomains(entities);
    }
}
