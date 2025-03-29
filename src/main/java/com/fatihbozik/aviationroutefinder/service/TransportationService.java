package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.dto.Transportation;

import java.util.List;

public interface TransportationService {
    List<Transportation> getTransportationsByDay(Integer originId, int day);
}
