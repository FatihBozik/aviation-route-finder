package com.fatihbozik.aviationroutefinder.mapper;

import com.fatihbozik.aviationroutefinder.domain.Transportation;
import com.fatihbozik.aviationroutefinder.persistence.TransportationEntity;
import com.fatihbozik.aviationroutefinder.rest.model.TransportationRequest;
import com.fatihbozik.aviationroutefinder.rest.model.TransportationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TransportationMapper {
    // REST ↔ Domain
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "originId", target = "origin.id")
    @Mapping(source = "destinationId", target = "destination.id")
    Transportation toDomain(TransportationRequest request);


    @Mapping(source = "origin.id", target = "originId")
    @Mapping(source = "destination.id", target = "destinationId")
    TransportationResponse toResponse(Transportation transportation);

    List<TransportationResponse> toResponses(Collection<Transportation> transportations);

    // Entity ↔ DOMAIN
    TransportationEntity toEntity(Transportation transportation);

    Transportation toDomain(TransportationEntity entity);

    List<Transportation> toDomains(List<TransportationEntity> transportationEntities);

    // Domain → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "origin.id", target = "origin.id")
    @Mapping(source = "destination.id", target = "destination.id")
    void updateEntity(Transportation newTransportation, @MappingTarget TransportationEntity entity);
}
