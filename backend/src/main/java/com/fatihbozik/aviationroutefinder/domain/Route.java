package com.fatihbozik.aviationroutefinder.domain;

import java.util.List;

public record Route(List<Transportation> steps) {
}
