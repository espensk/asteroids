package io.espens.spond.asteroids.controller;


import io.espens.spond.asteroids.controller.cache.HashmapCache;
import io.espens.spond.asteroids.controller.nasa.NeoWsClient;
import io.espens.spond.asteroids.controller.nasa.NeoWsClientTest;
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
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CachingAsteroidsProviderTest {

    @Mock private Client client;
    @Mock private Invocation.Builder requestBuilder;
    @Mock private WebTarget target;
    @Mock private Response mockResponse;

    private NeoWsClient neoWsClient;
    private HashmapCache cache;
    private CachingAsteroidsProvider cachingAsteroidsProvider;

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
        cache = new HashmapCache();
        cachingAsteroidsProvider = new CachingAsteroidsProvider(neoWsClient, cache);
    }

    @Test
    public void testAllInCache() throws AsteroidsException {
        when(mockResponse.readEntity(InputStream.class))
                .thenReturn(NeoWsClientTest.class.getResourceAsStream("/response-2015-09-07.json"))
                .thenReturn(NeoWsClientTest.class.getResourceAsStream("/response-2015-09-08.json"))
                .thenReturn(NeoWsClientTest.class.getResourceAsStream("/response-2015-09-09.json"));
        //First query is 3 days
        var asteroids = cachingAsteroidsProvider.getAsteroids(LocalDate.parse("2015-09-07"),
                LocalDate.parse ("2015-09-09"));
        assertThat(asteroids.size()).isEqualTo(37);
        verify(client, times(3)).target(any(URI.class));

        //2nd query 2 days
        asteroids = cachingAsteroidsProvider.getAsteroids(LocalDate.parse("2015-09-07"),
                LocalDate.parse ("2015-09-08"));
        assertThat(asteroids.size()).isEqualTo(25);
        verify(client, times(3)).target(any(URI.class));
    }

    @Test
    public void testNoneInCache() throws AsteroidsException {
        when(mockResponse.readEntity(InputStream.class))
                .thenReturn(NeoWsClientTest.class.getResourceAsStream("/response-2015-09-07.json"))
                .thenReturn(NeoWsClientTest.class.getResourceAsStream("/response-2015-09-08.json"))
                .thenReturn(NeoWsClientTest.class.getResourceAsStream("/response-2015-09-09.json"));
        //First query is 2 days
        var asteroids = cachingAsteroidsProvider.getAsteroids(LocalDate.parse("2015-09-07"),
                LocalDate.parse ("2015-09-08"));
        assertThat(asteroids.size()).isEqualTo(25);
        verify(client, times(2)).target(any(URI.class));

        //2nd query 2 days
        asteroids = cachingAsteroidsProvider.getAsteroids(LocalDate.parse("2015-09-09"),
                LocalDate.parse ("2015-09-09"));
        assertThat(asteroids.size()).isEqualTo(12);
        verify(client, times(3)).target(any(URI.class));
    }

    @Test
    public void testPartiallyInCache() throws AsteroidsException {
        when(mockResponse.readEntity(InputStream.class))
                .thenReturn(NeoWsClientTest.class.getResourceAsStream("/response-2015-09-07.json"))
                .thenReturn(NeoWsClientTest.class.getResourceAsStream("/response-2015-09-08.json"))
                .thenReturn(NeoWsClientTest.class.getResourceAsStream("/response-2015-09-09.json"));
        //First query is 2 days
        var asteroids = cachingAsteroidsProvider.getAsteroids(LocalDate.parse("2015-09-07"),
                LocalDate.parse ("2015-09-08"));
        assertThat(asteroids.size()).isEqualTo(25);
        verify(client, times(2)).target(any(URI.class));

        //2nd query 2 days
        asteroids = cachingAsteroidsProvider.getAsteroids(LocalDate.parse("2015-09-08"),
                LocalDate.parse ("2015-09-09"));
        assertThat(asteroids.size()).isEqualTo(24);
        verify(client, times(3)).target(any(URI.class));
    }
}