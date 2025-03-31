package com.fatihbozik.aviationroutefinder.service;

import com.fatihbozik.aviationroutefinder.domain.Location;
import com.fatihbozik.aviationroutefinder.mapper.LocationMapper;
import com.fatihbozik.aviationroutefinder.persistence.LocationEntity;
import com.fatihbozik.aviationroutefinder.repository.LocationRepository;
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
class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationServiceImpl locationService;

    private Location mockLocation;
    private LocationEntity mockLocationEntity;

    @BeforeEach
    void setUp() {
        mockLocation = new Location(1L, "Istanbul Airport", "Turkey", "Istanbul", "IST");

        mockLocationEntity = new LocationEntity();
        mockLocationEntity.setId(1L);
        mockLocationEntity.setName("Istanbul Airport");
        mockLocationEntity.setCode("IST");
        mockLocationEntity.setCity("Istanbul");
    }

    @Test
    void getAll_shouldReturnAllLocations() {
        List<LocationEntity> locationEntities = Collections.singletonList(mockLocationEntity);
        List<Location> expectedLocations = Collections.singletonList(mockLocation);

        when(locationRepository.findAll()).thenReturn(locationEntities);
        when(locationMapper.toDomains(locationEntities)).thenReturn(expectedLocations);

        List<Location> result = locationService.getAll();

        assertThat(result).isEqualTo(expectedLocations);
        verify(locationRepository).findAll();
        verify(locationMapper).toDomains(locationEntities);
    }

    @Test
    void create_shouldSaveAndReturnLocation() {
        when(locationMapper.toEntity(mockLocation)).thenReturn(mockLocationEntity);
        when(locationRepository.save(mockLocationEntity)).thenReturn(mockLocationEntity);
        when(locationMapper.toDomain(mockLocationEntity)).thenReturn(mockLocation);

        Location result = locationService.create(mockLocation);

        assertThat(result).isEqualTo(mockLocation);
        verify(locationMapper).toEntity(mockLocation);
        verify(locationRepository).save(mockLocationEntity);
        verify(locationMapper).toDomain(mockLocationEntity);
    }

    @Test
    void update_shouldUpdateAndReturnLocation() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(mockLocationEntity));
        doNothing().when(locationMapper).updateEntity(mockLocation, mockLocationEntity);
        when(locationRepository.save(mockLocationEntity)).thenReturn(mockLocationEntity);
        when(locationMapper.toDomain(mockLocationEntity)).thenReturn(mockLocation);

        Location result = locationService.update(1L, mockLocation);

        assertThat(result).isEqualTo(mockLocation);
        verify(locationRepository).findById(1L);
        verify(locationMapper).updateEntity(mockLocation, mockLocationEntity);
        verify(locationRepository).save(mockLocationEntity);
        verify(locationMapper).toDomain(mockLocationEntity);
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenLocationNotFound() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> locationService.update(999L, mockLocation))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Location not found with id: 999");

        verify(locationRepository).findById(999L);
        verify(locationMapper, never()).updateEntity(any(), any());
    }

    @Test
    void get_shouldReturnLocation() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(mockLocationEntity));
        when(locationMapper.toDomain(mockLocationEntity)).thenReturn(mockLocation);

        Location result = locationService.get(1L);

        assertThat(result).isEqualTo(mockLocation);
        verify(locationRepository).findById(1L);
        verify(locationMapper).toDomain(mockLocationEntity);
    }

    @Test
    void get_shouldThrowEntityNotFoundException_whenLocationNotFound() {
        when(locationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> locationService.get(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Location not found with id: 999");

        verify(locationRepository).findById(999L);
    }

    @Test
    void delete_shouldDeleteLocation() {
        when(locationRepository.existsById(1L)).thenReturn(true);

        locationService.delete(1L);

        verify(locationRepository).existsById(1L);
        verify(locationRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowEntityNotFoundException_whenLocationNotFound() {
        when(locationRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> locationService.delete(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Location not found with id: 999");

        verify(locationRepository).existsById(999L);
        verify(locationRepository, never()).deleteById(anyLong());
    }
}
