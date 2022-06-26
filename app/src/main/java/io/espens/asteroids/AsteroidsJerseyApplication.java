package io.espens.asteroids;

import io.espens.asteroids.api.AsteroidsApi;
import io.espens.asteroids.controller.AsteroidsProvider;
import io.espens.asteroids.controller.CachingAsteroidsProvider;
import io.espens.asteroids.controller.cache.AsteroidsCache;
import io.espens.asteroids.controller.cache.HashmapCache;
import io.espens.asteroids.controller.nasa.NeoWsClient;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

public class AsteroidsJerseyApplication extends ResourceConfig {
    public AsteroidsJerseyApplication() {
        register(AsteroidsApi.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(NeoWsClient.class).to(AsteroidsProvider.class).named("remote");
                bind(CachingAsteroidsProvider.class).to(AsteroidsProvider.class).named("caching");
                bind(HashmapCache.class).to(AsteroidsCache.class);
            }
        });
    }
}
