package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.domain.Location;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransportationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private TransportationMapper transportationMapper;

    @Mock
    private TransportationRepository transportationRepository;

    @InjectMocks
    private TransportationServiceImpl transportationService;

    private Transportation mockTransportation;
    private TransportationEntity mockTransportationEntity;
    private LocationEntity mockOriginEntity;
    private LocationEntity mockDestinationEntity;

    @BeforeEach
    void setUp() {
        Location mockOrigin = new Location(1L, "Istanbul Airport", "Turkey", "Istanbul", "IST");
        Location mockDestination = new Location(2L, "Ankara Airport", "Turkey", "Ankara", "ESB");

        mockOriginEntity = new LocationEntity();
        mockOriginEntity.setId(1L);
        mockOriginEntity.setName("Istanbul Airport");
        mockOriginEntity.setCountry("Turkey");
        mockOriginEntity.setCity("Istanbul");
        mockOriginEntity.setCode("IST");

        mockDestinationEntity = new LocationEntity();
        mockDestinationEntity.setId(2L);
        mockDestinationEntity.setName("Ankara Airport");
        mockDestinationEntity.setCountry("Turkey");
        mockDestinationEntity.setCity("Ankara");
        mockDestinationEntity.setCode("ESB");

        mockTransportation = new Transportation(
                1L,
                mockOrigin,
                mockDestination,
                TransportationType.FLIGHT,
                List.of(1, 3, 5)
        );

        mockTransportationEntity = new TransportationEntity();
        mockTransportationEntity.setId(1L);
        mockTransportationEntity.setOrigin(mockOriginEntity);
        mockTransportationEntity.setDestination(mockDestinationEntity);
        mockTransportationEntity.setType(TransportationType.FLIGHT);
        mockTransportationEntity.setOperatingDays(List.of(1, 3, 5));
    }

    @Test
    void getAll_shouldReturnAllTransportations() {
        List<TransportationEntity> transportationEntities = Collections.singletonList(mockTransportationEntity);
        List<Transportation> expectedTransportations = Collections.singletonList(mockTransportation);

        when(transportationRepository.findAll()).thenReturn(transportationEntities);
        when(transportationMapper.toDomains(transportationEntities)).thenReturn(expectedTransportations);

        List<Transportation> result = transportationService.getAll();

        assertThat(result).isEqualTo(expectedTransportations);
        verify(transportationRepository).findAll();
        verify(transportationMapper).toDomains(transportationEntities);
    }

    @Test
    void create_shouldSaveAndReturnTransportation() {
        when(transportationMapper.toEntity(mockTransportation)).thenReturn(mockTransportationEntity);
        when(transportationRepository.save(mockTransportationEntity)).thenReturn(mockTransportationEntity);
        when(transportationMapper.toDomain(mockTransportationEntity)).thenReturn(mockTransportation);

        Transportation result = transportationService.create(mockTransportation);

        assertThat(result).isEqualTo(mockTransportation);
        verify(transportationMapper).toEntity(mockTransportation);
        verify(transportationRepository).save(mockTransportationEntity);
        verify(transportationMapper).toDomain(mockTransportationEntity);
    }

    @Test
    void get_shouldReturnTransportation() {
        when(transportationRepository.findById(1L)).thenReturn(Optional.of(mockTransportationEntity));
        when(transportationMapper.toDomain(mockTransportationEntity)).thenReturn(mockTransportation);

        Transportation result = transportationService.get(1L);

        assertThat(result).isEqualTo(mockTransportation);
        verify(transportationRepository).findById(1L);
        verify(transportationMapper).toDomain(mockTransportationEntity);
    }

    @Test
    void get_shouldThrowEntityNotFoundException_whenTransportationNotFound() {
        when(transportationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transportationService.get(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Transportation not found with id: 999");

        verify(transportationRepository).findById(999L);
    }

    @Test
    void update_shouldUpdateAndReturnTransportation() {
        when(transportationRepository.findById(1L)).thenReturn(Optional.of(mockTransportationEntity));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(mockOriginEntity));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(mockDestinationEntity));
        when(transportationRepository.save(mockTransportationEntity)).thenReturn(mockTransportationEntity);
        when(transportationMapper.toDomain(mockTransportationEntity)).thenReturn(mockTransportation);

        Transportation result = transportationService.update(1L, mockTransportation);

        assertThat(result).isEqualTo(mockTransportation);
        verify(transportationRepository).findById(1L);
        verify(locationRepository).findById(1L);
        verify(locationRepository).findById(2L);
        verify(transportationRepository).save(mockTransportationEntity);
        verify(transportationMapper).toDomain(mockTransportationEntity);
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenTransportationNotFound() {
        when(transportationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transportationService.update(999L, mockTransportation))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Transportation not found with id: 999");

        verify(transportationRepository).findById(999L);
        verify(transportationRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenOriginLocationNotFound() {
        when(transportationRepository.findById(1L)).thenReturn(Optional.of(mockTransportationEntity));
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transportationService.update(1L, mockTransportation))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Location not found with id: 1");

        verify(transportationRepository).findById(1L);
        verify(locationRepository).findById(1L);
        verify(transportationRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenDestinationLocationNotFound() {
        when(transportationRepository.findById(1L)).thenReturn(Optional.of(mockTransportationEntity));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(mockOriginEntity));
        when(locationRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transportationService.update(1L, mockTransportation))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Location not found with id: 2");

        verify(transportationRepository).findById(1L);
        verify(locationRepository).findById(1L);
        verify(locationRepository).findById(2L);
        verify(transportationRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteTransportation() {
        when(transportationRepository.existsById(1L)).thenReturn(true);

        transportationService.delete(1L);

        verify(transportationRepository).existsById(1L);
        verify(transportationRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowEntityNotFoundException_whenTransportationNotFound() {
        when(transportationRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> transportationService.delete(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Transportation not found with id: 999");

        verify(transportationRepository).existsById(999L);
        verify(transportationRepository, never()).deleteById(anyLong());
    }
}
