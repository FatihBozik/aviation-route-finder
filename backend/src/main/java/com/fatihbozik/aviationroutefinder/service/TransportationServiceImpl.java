package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.domain.Transportation;
import com.fatihbozik.aviationroutefinder.mapper.TransportationMapper;
import com.fatihbozik.aviationroutefinder.persistence.TransportationEntity;
import com.fatihbozik.aviationroutefinder.repository.TransportationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportationServiceImpl implements TransportationService {
    private final TransportationRepository transportationRepository;
    private final TransportationMapper transportationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Transportation> getAll() {
        final List<TransportationEntity> transportationEntities = transportationRepository.findAll();
        return transportationMapper.toDomains(transportationEntities);
    }

    @Override
    @Transactional
    public Transportation create(Transportation transportation) {
        final TransportationEntity entity = transportationMapper.toEntity(transportation);
        final TransportationEntity savedEntity = transportationRepository.save(entity);
        return transportationMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transportation> getTransportationsByDay(Integer locationId, int day) {
        // TODO: Implement this method
//        return transportationRepository.findByOriginAndOperatingDaysContaining(null, day)
//                .stream()
//                .map(transportationMapper::toDomain)
//                .collect(Collectors.toList());
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Transportation get(Long id) {
        return transportationRepository.findById(id)
                .map(transportationMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Transportation not found with id: " + id));
    }

    @Override
    @Transactional
    public Transportation update(Long id, Transportation newTransportation) {
        final TransportationEntity entity = transportationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transportation not found with id: " + id));
        transportationMapper.updateEntity(newTransportation, entity);
        final TransportationEntity saved = transportationRepository.save(entity);
        return transportationMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void delete(Long transportationId) {
        if (!transportationRepository.existsById(transportationId)) {
            throw new EntityNotFoundException("Transportation not found with id: " + transportationId);
        }
        transportationRepository.deleteById(transportationId);
    }
}
