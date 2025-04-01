package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.domain.Location;
import com.fatihbozik.aviationroutefinder.mapper.LocationMapper;
import com.fatihbozik.aviationroutefinder.persistence.LocationEntity;
import com.fatihbozik.aviationroutefinder.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "locations", key = "'all'")
    public List<Location> getAll() {
        final List<LocationEntity> locationEntities = locationRepository.findAll();
        return locationMapper.toDomains(locationEntities);
    }

    @Override
    @Transactional
    @Caching(
            evict = {@CacheEvict(value = "locations", key = "'all'")},
            put = {@CachePut(value = "locations", key = "#result.id")}
    )
    public Location create(Location location) {
        final LocationEntity entity = locationMapper.toEntity(location);
        final LocationEntity savedEntity = locationRepository.save(entity);
        return locationMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "locations", key = "'all'"),
                    @CacheEvict(value = "routes", allEntries = true)
            },
            put = {@CachePut(value = "locations", key = "#id")}
    )
    public Location update(Long id, Location newData) {
        final LocationEntity entity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));
        locationMapper.updateEntity(newData, entity);
        final LocationEntity saved = locationRepository.save(entity);
        return locationMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "locations", key = "#id")
    public Location get(Long id) {
        return locationRepository.findById(id)
                .map(locationMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "locations", key = "#locationId"),
            @CacheEvict(value = "locations", key = "'all'"),
            @CacheEvict(value = "routes", allEntries = true)
    })
    public void delete(Long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new EntityNotFoundException("Location not found with id: " + locationId);
        }
        locationRepository.deleteById(locationId);
    }
}
