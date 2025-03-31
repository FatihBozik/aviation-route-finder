package com.fatihbozik.aviationroutefinder.persistence;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "transportations")
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

    @Type(ListArrayType.class)
    @Column(name = "operating_days", columnDefinition = "integer[]")
    private List<Integer> operatingDays;
}
