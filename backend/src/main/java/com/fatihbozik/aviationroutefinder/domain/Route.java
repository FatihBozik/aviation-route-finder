package com.fatihbozik.aviationroutefinder.domain;

import java.io.Serializable;
import java.util.List;

public record Route(List<Transportation> steps) implements Serializable {
}
