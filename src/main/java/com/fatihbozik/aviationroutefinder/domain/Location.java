package com.fatihbozik.aviationroutefinder.domain;

public record Location(
        Long id,
        String name,
        String country,
        String city,
        String code
) {
}