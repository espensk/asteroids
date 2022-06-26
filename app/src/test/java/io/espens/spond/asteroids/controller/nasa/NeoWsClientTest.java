package io.espens.spond.asteroids.controller.nasa;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.espens.spond.asteroids.api.model.Asteroid;
import io.espens.spond.asteroids.controller.AsteroidsException;
import io.espens.spond.asteroids.controller.nasa.model.NeowsResponse;
import jakarta.ws.rs.client.Client;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.commons.io.IOUtils;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NeoWsClientTest {
    @Mock private Client client;
    @Mock private Invocation.Builder requestBuilder;
    @Mock private WebTarget target;
    @Mock private Response mockResponse;

    private NeoWsClient neoWsClient;

    @BeforeEach
    public void setUp() {
        when(client.target(any(URI.class))).thenReturn(target);
        when(target.queryParam(any(), any())).thenReturn(target);
        when(target.request(MediaType.APPLICATION_JSON)).thenReturn(requestBuilder);
        when(requestBuilder.get()).thenReturn(mockResponse);

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getStatusInfo()).thenReturn(Response.Status.OK);
        when(mockResponse.readEntity(InputStream.class))
                .thenReturn(NeoWsClientTest.class.getResourceAsStream("/demo-response.json"));

        neoWsClient = new NeoWsClient(client);
    }

    @Test
    public void testDeserializeResponseFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        NeowsResponse response = mapper.readValue(
                NeoWsClientTest.class.getResourceAsStream("/demo-response.json"),
                NeowsResponse.class);
        assertThat(response).isNotNull();
    }

    @Test
    public void testDemoResponse() throws AsteroidsException {
        List<Asteroid> asteroids = neoWsClient.getAsteroids(
                LocalDate.parse("2015-09-07"),
                LocalDate.parse("2015-09-08"));
        assertThat(asteroids).isNotNull();
        assertThat(asteroids.size()).isEqualTo(25);
    }

    // utility: load a resource file as string
    private static String loadFile(String filename) {
        try (var inputStream = NeoWsClientTest.class.getResourceAsStream(filename)) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
        catch (IOException uh) {
            throw new IllegalArgumentException("Failed to load " + filename, uh);
        }
    }
}
