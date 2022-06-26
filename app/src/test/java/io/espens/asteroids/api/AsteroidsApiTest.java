package io.espens.asteroids.api;

import io.espens.asteroids.api.model.Asteroid;
import io.espens.asteroids.controller.nasa.NeoWsClient;
import io.espens.asteroids.controller.nasa.NeoWsClientTest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AsteroidsApiTest {
    @Mock
    private Client client;
    @Mock private Invocation.Builder requestBuilder;
    @Mock private WebTarget target;
    @Mock private Response mockResponse;

    private NeoWsClient neoWsClient;
    private AsteroidsApi api;

    @BeforeEach
    public void setUp() {
        when(client.target(any(URI.class))).thenReturn(target);
        when(target.queryParam(any(), any())).thenReturn(target);
        when(target.request(MediaType.APPLICATION_JSON)).thenReturn(requestBuilder);
        when(requestBuilder.get()).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.readEntity(InputStream.class))
                .thenReturn(NeoWsClientTest.class.getResourceAsStream("/demo-response.json"));

        neoWsClient = new NeoWsClient(client);
        api = new AsteroidsApi(neoWsClient);
    }

    /**
     * Asserts the biggest asteroid is found
     */
    @Test
    public void testBiggest() {
        Asteroid asteroid = api.getBiggest(2015);
        assertThat(asteroid.id()).isEqualTo("2440012");
        assertThat(asteroid.name()).isEqualTo("440012 (2002 LE27)");
        assertThat(asteroid.diameter()).isEqualTo("820.4270648822");
    }

    /**
     * Asserts the 3 closest flybys are found from the sample set
     */
    @Test
    public void test3Closest() {
        List<Asteroid> asteroids = api.close("2015-09-07","2015-09-08", 10);
        assertThat(asteroids).hasSize(10);
        Asteroid asteroid = asteroids.get(0);
        assertThat(asteroid.id()).isEqualTo("3726788");
        assertThat(asteroid.name()).isEqualTo("(2015 RG2)");
        assertThat(asteroid.distance()).isEqualTo("2450214.654065658");

        Iterator<Asteroid> iterator = asteroids.iterator();
        Asteroid last = iterator.next();
        while(iterator.hasNext()) {
            Asteroid curr = iterator.next();
            assertThat(curr.distance().compareTo(last.diameter())).isGreaterThan(0);
            last = curr;
        }

    }


}
