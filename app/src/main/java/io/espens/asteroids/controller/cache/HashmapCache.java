package io.espens.asteroids.controller.cache;

import io.espens.asteroids.api.model.Asteroid;
import io.espens.asteroids.controller.AsteroidsException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * Trivial cache implementation, storing as local in-memory hashmap
 */
public class HashmapCache implements AsteroidsCache {

    private final HashMap<LocalDate, List<Asteroid>> cache = new HashMap<>();

    @Override
    public List<Asteroid> getAsteroids(LocalDate date) throws AsteroidsException {
        if(cache.containsKey(date)) {
            return cache.get(date);
        }
        else {
            throw new AsteroidsException(date + " not found in cache");
        }
    }

    @Override
    public void storeAsteroids(LocalDate date, List<Asteroid> asteroids) {
        cache.put(date, asteroids);
    }
}
