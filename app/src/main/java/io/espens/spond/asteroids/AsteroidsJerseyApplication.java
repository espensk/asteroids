package io.espens.spond.asteroids;

import io.espens.spond.asteroids.api.AsteroidsApi;
import io.espens.spond.asteroids.controller.AsteroidsProvider;
import io.espens.spond.asteroids.controller.nasa.NeoWsClient;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

public class AsteroidsJerseyApplication extends ResourceConfig {
    public AsteroidsJerseyApplication() {
        register(AsteroidsApi.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(NeoWsClient.class).to(AsteroidsProvider.class);
            }
        });
    }
}
