package io.espens.asteroids.api.model;

import java.math.BigDecimal;
import java.net.URL;

/**
 * Documents an Asteroid, a simplified object from the Near Earth Object Web Service
 * @param id - the objects ID in NASA
 * @param name - the objects given name
 * @param diameter - the objects maximum estimated diameter in meters
 * @param distance - the minimum estimated distance from earth at closest fly-by, in kilometers
 * @param link - the URL to detailed object description
 */
public record Asteroid(String id, String name, BigDecimal diameter, BigDecimal distance, URL link) {
}
