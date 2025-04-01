package com.fatihbozik.aviationroutefinder.domain;

import com.fatihbozik.aviationroutefinder.persistence.TransportationType;

import java.io.Serializable;
import java.util.List;

public record Transportation(
        Long id,
        Location origin,
        Location destination,
        TransportationType type,
        List<Integer> operatingDays
) implements Serializable {
}