package io.espens.asteroids.controller.nasa;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.espens.asteroids.api.model.Asteroid;
import io.espens.asteroids.controller.AsteroidsProvider;
import io.espens.asteroids.controller.AsteroidsException;
import io.espens.asteroids.controller.nasa.model.NearEarthObject;
import io.espens.asteroids.controller.nasa.model.NeowsResponse;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.Response.*;

/**
 * Using NASAs Near Earth Object Web Service (NeoWS) APIs to provide asteroids
 */
public class NeoWsClient implements AsteroidsProvider {

    private static final URI NEOWS_URL = URI.create("https://api.nasa.gov/neo/rest/v1/feed");
    private static final String API_KEY = Optional.ofNullable(System.getenv("API_KEY")).orElse("DEMO_KEY");

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Client client;

    @SuppressWarnings("unused")
    public NeoWsClient() {
        this(ClientBuilder.newClient());
    }

    public NeoWsClient(Client client) {
        this.client = client;
    }

    @Override
    public List<Asteroid> getAsteroids(LocalDate start, LocalDate end) throws AsteroidsException {
        try {
            Response response = client
                    .target(NEOWS_URL)
                    .queryParam("start_date", start.format(DateTimeFormatter.ISO_DATE))
                    .queryParam("end_date", end.format(DateTimeFormatter.ISO_DATE))
                    .queryParam("api_key", API_KEY)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            if(response.getStatus() == Status.OK.getStatusCode()) {
                InputStream doc = response.readEntity(InputStream.class);
                return map(OBJECT_MAPPER.readValue(doc, NeowsResponse.class).near_earth_objects());
            }
            else {
                throw new AsteroidsException(String.format("NeoWs query resulted in %d %s: %s",
                        response.getStatus(), response.getStatusInfo().getReasonPhrase(), response.readEntity(String.class)));
            }
        }
        catch(ProcessingException | IOException uh) {
            throw new AsteroidsException("Failed to retrieve asteroids", uh);
        }
    }

    private List<Asteroid> map(Map<Date, List<NearEarthObject>> neos) {
        return neos.values().stream()
                .flatMap(Collection::stream)
                .map(this::map)
                .collect(Collectors.toList());

    }
    private Asteroid map(NearEarthObject neo) {
        //picking the closest fly-by
        return new Asteroid(neo.id(),
                neo.name(),
                neo.estimated_diameter().meters().estimated_diameter_max(),
                neo.close_approach_data().stream()
                        .map(d -> d.miss_distance().kilometers())
                        .min(BigDecimal::compareTo)
                        .get(),
                neo.nasa_jpl_url());
    }
}