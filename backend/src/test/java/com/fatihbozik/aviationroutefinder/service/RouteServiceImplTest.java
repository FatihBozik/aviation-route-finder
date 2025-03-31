package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.domain.Route;
import com.fatihbozik.aviationroutefinder.domain.Transportation;
import com.fatihbozik.aviationroutefinder.mapper.TransportationMapper;
import com.fatihbozik.aviationroutefinder.persistence.LocationEntity;
import com.fatihbozik.aviationroutefinder.persistence.TransportationEntity;
import com.fatihbozik.aviationroutefinder.persistence.TransportationType;
import com.fatihbozik.aviationroutefinder.repository.LocationRepository;
import com.fatihbozik.aviationroutefinder.repository.TransportationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RouteServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private TransportationRepository transportationRepository;

    @Mock
    private TransportationMapper transportationMapper;

    @InjectMocks
    private RouteServiceImpl routeService;

    private LocationEntity istanbulAirport;
    private LocationEntity heathrowAirport;
    private LocationEntity taksimSquare;
    private LocationEntity wembleyStadium;

    private TransportationEntity taksimToIstanbul;
    private TransportationEntity istanbulToHeathrow;
    private TransportationEntity heathrowToWembley;
    private TransportationEntity taksimToSaw;
    private TransportationEntity sawToHeathrow;

    private Transportation taksimToIstanbulDomain;
    private Transportation istanbulToHeathrowDomain;
    private Transportation heathrowToWembleyDomain;
    private Transportation taksimToSawDomain;
    private Transportation sawToHeathrowDomain;

    @BeforeEach
    void setUp() {
        // Set up location entities
        istanbulAirport = new LocationEntity();
        istanbulAirport.setId(1L);
        istanbulAirport.setName("Istanbul Airport");
        istanbulAirport.setCode("IST");
        istanbulAirport.setCity("Istanbul");
        istanbulAirport.setCountry("Turkey");

        LocationEntity sawAirport = new LocationEntity();
        sawAirport.setId(2L);
        sawAirport.setName("Sabiha Gökçen Airport");
        sawAirport.setCode("SAW");
        sawAirport.setCity("Istanbul");
        sawAirport.setCountry("Turkey");

        heathrowAirport = new LocationEntity();
        heathrowAirport.setId(3L);
        heathrowAirport.setName("Heathrow Airport");
        heathrowAirport.setCode("LHR");
        heathrowAirport.setCity("London");
        heathrowAirport.setCountry("UK");

        taksimSquare = new LocationEntity();
        taksimSquare.setId(4L);
        taksimSquare.setName("Taksim Square");
        taksimSquare.setCode("CCIST");
        taksimSquare.setCity("Istanbul");
        taksimSquare.setCountry("Turkey");

        wembleyStadium = new LocationEntity();
        wembleyStadium.setId(5L);
        wembleyStadium.setName("Wembley Stadium");
        wembleyStadium.setCode("CCLON");
        wembleyStadium.setCity("London");
        wembleyStadium.setCountry("UK");

        // Set up transportation entities
        taksimToIstanbul = new TransportationEntity();
        taksimToIstanbul.setId(1L);
        taksimToIstanbul.setOrigin(taksimSquare);
        taksimToIstanbul.setDestination(istanbulAirport);
        taksimToIstanbul.setType(TransportationType.BUS);
        taksimToIstanbul.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5, 6, 7));

        istanbulToHeathrow = new TransportationEntity();
        istanbulToHeathrow.setId(2L);
        istanbulToHeathrow.setOrigin(istanbulAirport);
        istanbulToHeathrow.setDestination(heathrowAirport);
        istanbulToHeathrow.setType(TransportationType.FLIGHT);
        istanbulToHeathrow.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5));

        heathrowToWembley = new TransportationEntity();
        heathrowToWembley.setId(3L);
        heathrowToWembley.setOrigin(heathrowAirport);
        heathrowToWembley.setDestination(wembleyStadium);
        heathrowToWembley.setType(TransportationType.UBER);
        heathrowToWembley.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5, 6, 7));

        taksimToSaw = new TransportationEntity();
        taksimToSaw.setId(4L);
        taksimToSaw.setOrigin(taksimSquare);
        taksimToSaw.setDestination(sawAirport);
        taksimToSaw.setType(TransportationType.BUS);
        taksimToSaw.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5));

        sawToHeathrow = new TransportationEntity();
        sawToHeathrow.setId(5L);
        sawToHeathrow.setOrigin(sawAirport);
        sawToHeathrow.setDestination(heathrowAirport);
        sawToHeathrow.setType(TransportationType.FLIGHT);
        sawToHeathrow.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5));

        // Set up domain objects for mapper
        taksimToIstanbulDomain = mock(Transportation.class);
        istanbulToHeathrowDomain = mock(Transportation.class);
        heathrowToWembleyDomain = mock(Transportation.class);
        taksimToSawDomain = mock(Transportation.class);
        sawToHeathrowDomain = mock(Transportation.class);
    }

    @Test
    void calculateRoutes_LocationNotFound_ThrowsEntityNotFoundException() {
        when(locationRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            routeService.calculateRoutes("UNKNOWN", "LHR", 1);
        });

        verify(locationRepository).findByCode("UNKNOWN");
    }

    @Test
    void calculateRoutes_DestinationNotFound_ThrowsEntityNotFoundException() {
        when(locationRepository.findByCode("CCIST")).thenReturn(Optional.of(taksimSquare));
        when(locationRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            routeService.calculateRoutes("CCIST", "UNKNOWN", 1);
        });

        verify(locationRepository).findByCode("CCIST");
        verify(locationRepository).findByCode("UNKNOWN");
    }

    @Test
    void calculateRoutes_NoValidRoutes_ReturnsEmptyList() {
        when(locationRepository.findByCode("CCIST")).thenReturn(Optional.of(taksimSquare));
        when(locationRepository.findByCode("CCLON")).thenReturn(Optional.of(wembleyStadium));
        when(transportationRepository.findAll()).thenReturn(new ArrayList<>());

        List<Route> routes = routeService.calculateRoutes("CCIST", "CCLON", 1);

        assertTrue(routes.isEmpty());
        verify(locationRepository).findByCode("CCIST");
        verify(locationRepository).findByCode("CCLON");
        verify(transportationRepository).findAll();
    }

    @Test
    void calculateRoutes_ValidSingleRoute_ReturnsOneRoute() {
        when(locationRepository.findByCode("IST")).thenReturn(Optional.of(istanbulAirport));
        when(locationRepository.findByCode("LHR")).thenReturn(Optional.of(heathrowAirport));
        when(transportationRepository.findAll()).thenReturn(List.of(istanbulToHeathrow));

        when(transportationMapper.toDomains(List.of(istanbulToHeathrow)))
                .thenReturn(List.of(istanbulToHeathrowDomain));

        List<Route> routes = routeService.calculateRoutes("IST", "LHR", 1);

        assertEquals(1, routes.size());
        verify(locationRepository).findByCode("IST");
        verify(locationRepository).findByCode("LHR");
        verify(transportationRepository).findAll();
        verify(transportationMapper).toDomains(anyList());
    }

    @Test
    void calculateRoutes_ValidMultiRoutes_ReturnsMultipleRoutes() {
        when(locationRepository.findByCode("CCIST")).thenReturn(Optional.of(taksimSquare));
        when(locationRepository.findByCode("CCLON")).thenReturn(Optional.of(wembleyStadium));

        when(transportationRepository.findAll()).thenReturn(Arrays.asList(
                taksimToIstanbul, istanbulToHeathrow, heathrowToWembley,
                taksimToSaw, sawToHeathrow
        ));

        when(transportationMapper.toDomains(Arrays.asList(taksimToIstanbul, istanbulToHeathrow, heathrowToWembley)))
                .thenReturn(Arrays.asList(taksimToIstanbulDomain, istanbulToHeathrowDomain, heathrowToWembleyDomain));

        when(transportationMapper.toDomains(Arrays.asList(taksimToSaw, sawToHeathrow, heathrowToWembley)))
                .thenReturn(Arrays.asList(taksimToSawDomain, sawToHeathrowDomain, heathrowToWembleyDomain));

        List<Route> routes = routeService.calculateRoutes("CCIST", "CCLON", 1);

        assertEquals(2, routes.size());
        verify(locationRepository).findByCode("CCIST");
        verify(locationRepository).findByCode("CCLON");
        verify(transportationRepository).findAll();
        verify(transportationMapper, atLeast(1)).toDomains(anyList());
    }

    @Test
    void calculateRoutes_TooManyTransportations_ReturnsValidRoutesOnly() {
        LocationEntity parisAirport = new LocationEntity();
        parisAirport.setId(6L);
        parisAirport.setName("Charles de Gaulle Airport");
        parisAirport.setCode("CDG");
        parisAirport.setCity("Paris");
        parisAirport.setCountry("France");

        TransportationEntity heathrowToParis = new TransportationEntity();
        heathrowToParis.setId(6L);
        heathrowToParis.setOrigin(heathrowAirport);
        heathrowToParis.setDestination(parisAirport);
        heathrowToParis.setType(TransportationType.BUS);
        heathrowToParis.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5));

        TransportationEntity parisToWembley = new TransportationEntity();
        parisToWembley.setId(7L);
        parisToWembley.setOrigin(parisAirport);
        parisToWembley.setDestination(wembleyStadium);
        parisToWembley.setType(TransportationType.BUS);
        parisToWembley.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5));

        when(locationRepository.findByCode("CCIST")).thenReturn(Optional.of(taksimSquare));
        when(locationRepository.findByCode("CDG")).thenReturn(Optional.of(parisAirport));

        when(transportationRepository.findAll()).thenReturn(Arrays.asList(
                taksimToIstanbul, istanbulToHeathrow, heathrowToParis, parisToWembley
        ));

        when(transportationMapper.toDomains(Arrays.asList(taksimToIstanbul, istanbulToHeathrow, heathrowToParis)))
                .thenReturn(Arrays.asList(taksimToIstanbulDomain, istanbulToHeathrowDomain, mock(Transportation.class)));

        List<Route> routes = routeService.calculateRoutes("CCIST", "CDG", 1);

        assertEquals(1, routes.size());
        verify(locationRepository).findByCode("CCIST");
        verify(locationRepository).findByCode("CDG");
        verify(transportationRepository).findAll();
    }

    @Test
    void calculateRoutes_NoFlight_ReturnsEmptyList() {
        TransportationEntity taksimToWembley = new TransportationEntity();
        taksimToWembley.setId(8L);
        taksimToWembley.setOrigin(taksimSquare);
        taksimToWembley.setDestination(wembleyStadium);
        taksimToWembley.setType(TransportationType.BUS);
        taksimToWembley.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5));

        when(locationRepository.findByCode("CCIST")).thenReturn(Optional.of(taksimSquare));
        when(locationRepository.findByCode("CCLON")).thenReturn(Optional.of(wembleyStadium));
        when(transportationRepository.findAll()).thenReturn(List.of(taksimToWembley));

        List<Route> routes = routeService.calculateRoutes("CCIST", "CCLON", 1);

        assertTrue(routes.isEmpty());
        verify(locationRepository).findByCode("CCIST");
        verify(locationRepository).findByCode("CCLON");
        verify(transportationRepository).findAll();
    }

    @Test
    void calculateRoutes_MultipleFlights_ReturnsEmptyList() {
        TransportationEntity taksimToIst = new TransportationEntity();
        taksimToIst.setId(1L);
        taksimToIst.setOrigin(taksimSquare);
        taksimToIst.setDestination(istanbulAirport);
        taksimToIst.setType(TransportationType.BUS);
        taksimToIst.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5, 6, 7));

        TransportationEntity istToLhr = new TransportationEntity();
        istToLhr.setId(2L);
        istToLhr.setOrigin(istanbulAirport);
        istToLhr.setDestination(heathrowAirport);
        istToLhr.setType(TransportationType.FLIGHT);
        istToLhr.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5));

        TransportationEntity lhrToWembley = new TransportationEntity();
        lhrToWembley.setId(3L);
        lhrToWembley.setOrigin(heathrowAirport);
        lhrToWembley.setDestination(wembleyStadium);
        lhrToWembley.setType(TransportationType.FLIGHT); // İkinci uçuş burada, bu da testi başarısız kılmalı
        lhrToWembley.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5, 6, 7));

        when(locationRepository.findByCode("CCIST")).thenReturn(Optional.of(taksimSquare));
        when(locationRepository.findByCode("CCLON")).thenReturn(Optional.of(wembleyStadium));

        when(transportationRepository.findAll()).thenReturn(Arrays.asList(
                taksimToIst, istToLhr, lhrToWembley
        ));

        List<Route> routes = routeService.calculateRoutes("CCIST", "CCLON", 1);

        assertTrue(routes.isEmpty(), "Birden fazla uçuş içeren rotalar boş bir liste döndürmelidir");
        verify(locationRepository).findByCode("CCIST");
        verify(locationRepository).findByCode("CCLON");
        verify(transportationRepository).findAll();

        verifyNoInteractions(transportationMapper);
    }

    @Test
    void calculateRoutes_MultipleBeforeFlightTransfers_ReturnsEmptyList() {
        LocationEntity antalyaAirport = new LocationEntity();
        antalyaAirport.setId(10L);
        antalyaAirport.setName("Antalya Airport");
        antalyaAirport.setCode("AYT");
        antalyaAirport.setCity("Antalya");
        antalyaAirport.setCountry("Turkey");

        TransportationEntity taksimToAntalya = new TransportationEntity();
        taksimToAntalya.setId(10L);
        taksimToAntalya.setOrigin(taksimSquare);
        taksimToAntalya.setDestination(antalyaAirport);
        taksimToAntalya.setType(TransportationType.BUS);
        taksimToAntalya.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5));

        TransportationEntity antalyaToIstanbul = new TransportationEntity();
        antalyaToIstanbul.setId(11L);
        antalyaToIstanbul.setOrigin(antalyaAirport);
        antalyaToIstanbul.setDestination(istanbulAirport);
        antalyaToIstanbul.setType(TransportationType.BUS);
        antalyaToIstanbul.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5));

        when(locationRepository.findByCode("CCIST")).thenReturn(Optional.of(taksimSquare));
        when(locationRepository.findByCode("LHR")).thenReturn(Optional.of(heathrowAirport));

        when(transportationRepository.findAll()).thenReturn(Arrays.asList(
                taksimToAntalya, antalyaToIstanbul, istanbulToHeathrow
        ));

        List<Route> routes = routeService.calculateRoutes("CCIST", "LHR", 1);

        assertTrue(routes.isEmpty());
        verify(locationRepository).findByCode("CCIST");
        verify(locationRepository).findByCode("LHR");
        verify(transportationRepository).findAll();
    }

    @Test
    void calculateRoutes_MultipleAfterFlightTransfers_ReturnsEmptyList() {
        LocationEntity manchesterAirport = new LocationEntity();
        manchesterAirport.setId(11L);
        manchesterAirport.setName("Manchester Airport");
        manchesterAirport.setCode("MAN");
        manchesterAirport.setCity("Manchester");
        manchesterAirport.setCountry("UK");

        TransportationEntity heathrowToManchester = new TransportationEntity();
        heathrowToManchester.setId(12L);
        heathrowToManchester.setOrigin(heathrowAirport);
        heathrowToManchester.setDestination(manchesterAirport);
        heathrowToManchester.setType(TransportationType.BUS);
        heathrowToManchester.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5));

        TransportationEntity manchesterToWembley = new TransportationEntity();
        manchesterToWembley.setId(13L);
        manchesterToWembley.setOrigin(manchesterAirport);
        manchesterToWembley.setDestination(wembleyStadium);
        manchesterToWembley.setType(TransportationType.BUS);
        manchesterToWembley.setOperatingDays(Arrays.asList(1, 2, 3, 4, 5));

        when(locationRepository.findByCode("IST")).thenReturn(Optional.of(istanbulAirport));
        when(locationRepository.findByCode("CCLON")).thenReturn(Optional.of(wembleyStadium));

        when(transportationRepository.findAll()).thenReturn(Arrays.asList(
                istanbulToHeathrow, heathrowToManchester, manchesterToWembley
        ));

        List<Route> routes = routeService.calculateRoutes("IST", "CCLON", 1);

        assertTrue(routes.isEmpty());
        verify(locationRepository).findByCode("IST");
        verify(locationRepository).findByCode("CCLON");
        verify(transportationRepository).findAll();
    }

    @Test
    void calculateRoutes_InvalidOperatingDay_ReturnsEmptyList() {
        when(locationRepository.findByCode("IST")).thenReturn(Optional.of(istanbulAirport));
        when(locationRepository.findByCode("LHR")).thenReturn(Optional.of(heathrowAirport));

        TransportationEntity flightWithSpecificDays = new TransportationEntity();
        flightWithSpecificDays.setId(14L);
        flightWithSpecificDays.setOrigin(istanbulAirport);
        flightWithSpecificDays.setDestination(heathrowAirport);
        flightWithSpecificDays.setType(TransportationType.FLIGHT);
        flightWithSpecificDays.setOperatingDays(Arrays.asList(1, 3, 5)); // Mon, Wed, Fri

        when(transportationRepository.findAll()).thenReturn(List.of(flightWithSpecificDays));

        List<Route> routes = routeService.calculateRoutes("IST", "LHR", 2);

        assertTrue(routes.isEmpty());
        verify(locationRepository).findByCode("IST");
        verify(locationRepository).findByCode("LHR");
        verify(transportationRepository).findAll();
    }
}
