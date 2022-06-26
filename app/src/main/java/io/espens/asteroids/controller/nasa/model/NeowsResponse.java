package io.espens.asteroids.controller.nasa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NeowsResponse(int element_count,
                            Map<Date, List<NearEarthObject>> near_earth_objects) {
}
