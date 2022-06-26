package io.espens.spond.asteroids.controller.cache;

import io.espens.spond.asteroids.api.model.Asteroid;
import io.espens.spond.asteroids.controller.AsteroidsException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * Trivial cache implementation, storing as local in-memory hashmap
 */
public class HashmapCache implements AsteroidsCache {

    private HashMap<LocalDate, List<Asteroid>> cache = new HashMap<>();

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
    public void storeAsteroids(LocalDate date, List<Asteroid> asteroids) throws AsteroidsException {
        cache.put(date, asteroids);
    }
}
