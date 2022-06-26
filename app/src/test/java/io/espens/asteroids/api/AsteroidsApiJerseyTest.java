package io.espens.asteroids.api;

import io.espens.asteroids.api.model.Asteroid;
import io.espens.asteroids.controller.AsteroidsProvider;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AsteroidsApiJerseyTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig()
                .register(AsteroidsApi.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(DummyAsteroidProvider.class).to(AsteroidsProvider.class).named("caching");
                    }
                });
    }

    @Test
    public void testBiggest() {
        Response response = target("asteroids/biggest")
                .queryParam("year", 1992)
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.hasEntity()).isTrue();
        Asteroid asteroid = response.readEntity(Asteroid.class);
        assertThat(asteroid).isNotNull();
    }

    @Test
    public void testClose() {
        Response response = target("asteroids/close")
                .queryParam("start_date", "2020-06-10")
                .queryParam("end_date", "2020-06-17")
                .queryParam("limit", 10)
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.hasEntity()).isTrue();
        List<Asteroid> asteroids = response.readEntity(new GenericType<List<Asteroid>>() {});
        assertThat(asteroids).isNotNull();
        assertThat(asteroids).isNotEmpty();
    }

    @Test
    public void testCloseWithBadDates() {
        Response response = target("asteroids/close")
                .queryParam("start_date", "yesterday")
                .queryParam("end_date", "tomorrow")
                .queryParam("limit", 10)
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus()).isEqualTo(400);
    }
}
