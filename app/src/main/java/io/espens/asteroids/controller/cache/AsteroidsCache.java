package io.espens.asteroids.controller.cache;

import io.espens.asteroids.api.model.Asteroid;
import io.espens.asteroids.controller.AsteroidsException;

import java.time.LocalDate;
import java.util.List;

public interface AsteroidsCache {
    /** Get the stored lists of asteroids for a single day.
     * @param date the date to query
     * @return a list of asteroids.
     * Empty list means we have queried before and received no asteroid passings
     * @exception AsteroidsException
     * if no record is found in cache, or we fail to contact cache.
     */
    List<Asteroid> getAsteroids(LocalDate date) throws AsteroidsException;

    /** Store a list of asteroids for a single day.
     * @param date the date to store
     * @param asteroids  a list of asteroids.
     * Empty list means we have queried before and received no asteroid passings
     * @exception AsteroidsException
     * if we fail to contact cache.
     */
    void storeAsteroids(LocalDate date, List<Asteroid> asteroids) throws AsteroidsException;
}
