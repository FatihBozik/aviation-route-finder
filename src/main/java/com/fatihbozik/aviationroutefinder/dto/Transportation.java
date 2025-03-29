package com.fatihbozik.aviationroutefinder.dto;

import com.fatihbozik.aviationroutefinder.model.TransportationEntity;
import com.fatihbozik.aviationroutefinder.model.TransportationType;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class Transportation {
    private final Location origin;
    private final Location destination;
    private final TransportationType type;
    private final List<Integer> operatingDays;

    public Transportation(TransportationEntity transportationEntity) {
        this.origin = new Location(transportationEntity.getOrigin());
        this.destination = new Location(transportationEntity.getDestination());
        this.type = transportationEntity.getType();
        this.operatingDays = transportationEntity.getOperatingDays();
    }
}
