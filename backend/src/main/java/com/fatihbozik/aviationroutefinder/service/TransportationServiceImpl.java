package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.domain.Transportation;
import com.fatihbozik.aviationroutefinder.mapper.TransportationMapper;
import com.fatihbozik.aviationroutefinder.persistence.LocationEntity;
import com.fatihbozik.aviationroutefinder.persistence.TransportationEntity;
import com.fatihbozik.aviationroutefinder.repository.LocationRepository;
import com.fatihbozik.aviationroutefinder.repository.TransportationRepository;
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
public class TransportationServiceImpl implements TransportationService {
    private final LocationRepository locationRepository;
    private final TransportationMapper transportationMapper;
    private final TransportationRepository transportationRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "transportations", key = "'all'")
    public List<Transportation> getAll() {
        final List<TransportationEntity> transportationEntities = transportationRepository.findAll();
        return transportationMapper.toDomains(transportationEntities);
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "transportations", key = "'all'"),
                    @CacheEvict(value = "routes", allEntries = true)
            },
            put = {@CachePut(value = "transportations", key = "#result.id")}
    )
    public Transportation create(Transportation transportation) {
        final TransportationEntity entity = transportationMapper.toEntity(transportation);
        final TransportationEntity savedEntity = transportationRepository.save(entity);
        return transportationMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "transportations", key = "#id")
    public Transportation get(Long id) {
        return transportationRepository.findById(id)
                .map(transportationMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Transportation not found with id: " + id));
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "transportations", key = "'all'"),
                    @CacheEvict(value = "routes", allEntries = true)
            },
            put = { @CachePut(value = "transportations", key = "#id") }
    )
    public Transportation update(Long id, Transportation newTransportation) {
        final TransportationEntity entity = getTransportation(id);
        patchEntity(newTransportation, entity);
        final TransportationEntity saved = transportationRepository.save(entity);
        return transportationMapper.toDomain(saved);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "transportations", key = "#transportationId"),
            @CacheEvict(value = "transportations", key = "'all'"),
            @CacheEvict(value = "routes", allEntries = true)
    })
    public void delete(Long transportationId) {
        if (!transportationRepository.existsById(transportationId)) {
            throw new EntityNotFoundException("Transportation not found with id: " + transportationId);
        }
        transportationRepository.deleteById(transportationId);
    }

    private void patchEntity(Transportation newTransportation, TransportationEntity entity) {
        final LocationEntity originEntity = getLocation(newTransportation.origin().id());
        entity.setOrigin(originEntity);

        final LocationEntity destinationEntity = getLocation(newTransportation.destination().id());
        entity.setDestination(destinationEntity);

        entity.setType(newTransportation.type());
        entity.setOperatingDays(newTransportation.operatingDays());
    }

    private LocationEntity getLocation(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));
    }

    private TransportationEntity getTransportation(Long id) {
        return transportationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transportation not found with id: " + id));
    }
}
