package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.domain.Route;

import java.util.List;

public interface RouteService {

    List<Route> calculateRoutes(String originCode, String destinationCode, int day);
}
