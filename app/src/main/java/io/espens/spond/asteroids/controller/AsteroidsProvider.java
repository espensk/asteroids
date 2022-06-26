package io.espens.spond.asteroids.controller;

import io.espens.spond.asteroids.api.model.Asteroid;

import java.time.LocalDate;
import java.util.List;

public interface AsteroidsProvider {

    /**
     * Get a list of asteroids passing earth between the start and end dates provided.
     * @param start first date of interval
     * @param end last date of interval
     * @return the full list of asteroid passings.
     */
    List<Asteroid> getAsteroids(LocalDate start, LocalDate end) throws AsteroidsException;
}
