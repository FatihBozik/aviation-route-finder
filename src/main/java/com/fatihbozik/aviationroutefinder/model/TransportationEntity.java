package com.fatihbozik.aviationroutefinder.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "transportation")
public class TransportationEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "origin_location_id", nullable = false)
    private LocationEntity origin;

    @ManyToOne
    @JoinColumn(name = "destination_location_id", nullable = false)
    private LocationEntity destination;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransportationType type;

    @ElementCollection
    private List<Integer> operatingDays;
}
