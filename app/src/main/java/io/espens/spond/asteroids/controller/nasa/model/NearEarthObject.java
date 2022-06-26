package io.espens.spond.asteroids.controller.nasa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NearEarthObject(String id,
                              String neo_reference_id,
                              String name,
                              URL nasa_jpl_url,
                              BigDecimal absolute_magnitude_h,
                              EstimatedDiameter estimated_diameter,
                              boolean is_potentially_hazardous_asteroid,
                              List<CloseApproachData> close_approach_data,
                              boolean is_sentry_object) {
}
