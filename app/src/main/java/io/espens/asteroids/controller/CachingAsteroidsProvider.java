package io.espens.asteroids.controller;

import io.espens.asteroids.api.model.Asteroid;
import io.espens.asteroids.controller.cache.AsteroidsCache;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * A caching and iterating Asteroids Provider.
 * Will divide the search in 1 day bulks and check cache
 */
public class CachingAsteroidsProvider implements AsteroidsProvider {

    private static final Logger LOG = LoggerFactory.getLogger(CachingAsteroidsProvider.class);

    private final AsteroidsProvider remoteProvider;
    private final AsteroidsCache cache;

    @Inject
    public CachingAsteroidsProvider(@Named("remote") AsteroidsProvider remoteProvider,
                                    AsteroidsCache cache) {
        this.remoteProvider = remoteProvider;
        this.cache = cache;
    }

    /**
     * Iterates day-by-day and queries for asteroids per day.
     * @param start first date of interval
     * @param end last date of interval
     * @return List of asteroids
     * @throws AsteroidsException
     */
    @Override
    public List<Asteroid> getAsteroids(LocalDate start, LocalDate end) throws AsteroidsException {
        LocalDate curr = start;
        List<Asteroid> all = new ArrayList<>();
        while(!curr.isAfter(end)) {
            all.addAll(getAsteroids(curr));
            curr = curr.plus(1, ChronoUnit.DAYS);
        }
        return all;
    }

    public List<Asteroid> getAsteroids(LocalDate date) throws AsteroidsException {
        try {
            return cache.getAsteroids(date);
        } catch (AsteroidsException e) {
            LOG.info("Failed to find {} in cache, get from remote due {}", date, e.getMessage());
            List<Asteroid> remotes = remoteProvider.getAsteroids(date, date);
            LOG.info("Retrieved {} asteroids from remote", remotes.size());
            try {
                cache.storeAsteroids(date, remotes);
            }
            catch(AsteroidsException uh) {
                LOG.warn("Failed to store result in cache, ignoring", uh);
            }
            return remotes;
        }
    }
}
