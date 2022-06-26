package io.espens.spond.asteroids;

import io.espens.spond.asteroids.api.AsteroidsApi;
import org.glassfish.jersey.server.ResourceConfig;

public class AsteroidsJerseyApplication extends ResourceConfig {
    public AsteroidsJerseyApplication() {
        register(AsteroidsApi.class);
    }
}
