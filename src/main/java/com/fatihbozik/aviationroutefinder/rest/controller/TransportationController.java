package com.fatihbozik.aviationroutefinder.rest.controller;

import com.fatihbozik.aviationroutefinder.dto.Transportation;
import com.fatihbozik.aviationroutefinder.service.TransportationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transportations")
public class TransportationController {
    private final TransportationService transportationService;

    @GetMapping("/{originId}/{day}")
    public List<Transportation> getTransportations(
            @PathVariable Integer originId,
            @PathVariable int day) {
        return transportationService.getTransportationsByDay(originId, day);
    }
}
