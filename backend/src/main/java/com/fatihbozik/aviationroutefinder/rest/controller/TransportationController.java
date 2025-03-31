package com.fatihbozik.aviationroutefinder.rest.controller;

import com.fatihbozik.aviationroutefinder.domain.Transportation;
import com.fatihbozik.aviationroutefinder.mapper.TransportationMapper;
import com.fatihbozik.aviationroutefinder.rest.model.TransportationRequest;
import com.fatihbozik.aviationroutefinder.rest.model.TransportationResponse;
import com.fatihbozik.aviationroutefinder.service.TransportationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transportations")
public class TransportationController {
    private final TransportationService transportationService;
    private final TransportationMapper transportationMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole(@roles.ADMIN)")
    public List<TransportationResponse> getAllTransportations() {
        List<Transportation> allTransportations = transportationService.getAll();
        return transportationMapper.toResponses(allTransportations);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole(@roles.ADMIN)")
    public TransportationResponse createTransportation(@RequestBody @Valid TransportationRequest request) {
        final Transportation transportation = transportationMapper.toDomain(request);
        final Transportation createdTransportation = transportationService.create(transportation);
        return transportationMapper.toResponse(createdTransportation);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole(@roles.ADMIN)")
    public TransportationResponse getTransportation(@PathVariable("id") Long transportationId) {
        final Transportation transportation = transportationService.get(transportationId);
        return transportationMapper.toResponse(transportation);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole(@roles.ADMIN)")
    public TransportationResponse updateTransportation(@PathVariable("id") Long transportationId, @RequestBody TransportationRequest request) {
        final Transportation transportation = transportationMapper.toDomain(request);
        final Transportation updatedTransportation = transportationService.update(transportationId, transportation);
        return transportationMapper.toResponse(updatedTransportation);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole(@roles.ADMIN)")
    public void deleteTransportation(@PathVariable("id") Long transportationId) {
        transportationService.delete(transportationId);
    }
}
