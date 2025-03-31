package com.fatihbozik.aviationroutefinder.mapper;

import com.fatihbozik.aviationroutefinder.domain.Location;
import com.fatihbozik.aviationroutefinder.persistence.LocationEntity;
import com.fatihbozik.aviationroutefinder.rest.model.LocationRequest;
import com.fatihbozik.aviationroutefinder.rest.model.LocationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    // REST ↔ Domain
    @Mapping(target = "id", ignore = true)
    Location toDomain(LocationRequest request);

    LocationResponse toResponse(Location location);

    List<LocationResponse> toResponses(Collection<Location> locations);

    // Entity ↔ DOMAIN
    LocationEntity toEntity(Location location);

    Location toDomain(LocationEntity entity);

    List<Location> toDomains(Collection<LocationEntity> entities);

    // Domain → Entity
    @Mapping(target = "id", ignore = true)
    void updateEntity(Location newLocation, @MappingTarget LocationEntity entity);
}
