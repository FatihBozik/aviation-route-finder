package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.domain.Transportation;

import java.util.List;

public interface TransportationService {
    List<Transportation> getAll();

    Transportation create(Transportation transportation);

    Transportation get(Long transportationId);

    Transportation update(Long transportationId, Transportation transportation);

    void delete(Long locationId);
}
