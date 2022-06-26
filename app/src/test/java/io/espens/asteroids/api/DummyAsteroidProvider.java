package io.espens.asteroids.api;

import io.espens.asteroids.api.model.Asteroid;
import io.espens.asteroids.controller.AsteroidsProvider;
import io.espens.asteroids.controller.AsteroidsException;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class DummyAsteroidProvider implements AsteroidsProvider {
    @Override
    public List<Asteroid> getAsteroids(LocalDate start, LocalDate end) throws AsteroidsException {
        try {
            return List.of(new Asteroid("dummy",
                    "dummy-asteroid",
                    new BigDecimal("32.312312"),
                    new BigDecimal(32),
                    new URL("https://nasa.gov/dummy")));
        } catch (MalformedURLException e) {
            throw new AsteroidsException("bad url", e);
        }
    }
}
