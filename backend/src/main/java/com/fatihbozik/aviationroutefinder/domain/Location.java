package com.fatihbozik.aviationroutefinder.domain;

import java.io.Serializable;

public record Location(
        Long id,
        String name,
        String country,
        String city,
        String code
) implements Serializable {
}