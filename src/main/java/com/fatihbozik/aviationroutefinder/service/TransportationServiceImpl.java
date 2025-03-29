package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.dto.Transportation;
import com.fatihbozik.aviationroutefinder.repository.TransportationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransportationServiceImpl implements TransportationService {
    private final TransportationRepository transportationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Transportation> getTransportationsByDay(Integer locationId, int day) {
        return transportationRepository.findByOriginAndOperatingDaysContaining(null, day)
                .stream()
                .map(Transportation::new)
                .collect(Collectors.toList());
    }
}
