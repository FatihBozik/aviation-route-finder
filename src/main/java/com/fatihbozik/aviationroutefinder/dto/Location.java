package com.fatihbozik.aviationroutefinder.dto;

import com.fatihbozik.aviationroutefinder.model.LocationEntity;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Location {
    private final String name;
    private final String country;
    private final String city;
    private final String locationCode;

    public Location(LocationEntity locationEntity) {
        this.name = locationEntity.getName();
        this.country = locationEntity.getCountry();
        this.city = locationEntity.getCity();
        this.locationCode = locationEntity.getLocationCode();
    }
}
