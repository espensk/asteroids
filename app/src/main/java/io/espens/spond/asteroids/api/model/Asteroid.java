package io.espens.spond.asteroids.api.model;

import java.math.BigDecimal;
import java.net.URL;

public record Asteroid(String id, String name, BigDecimal diameter, BigDecimal distance, URL link) {
}
