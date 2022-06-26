package io.espens.spond.asteroids.controller.nasa.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CloseApproachData(Date close_approach_date,
                                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMMM-dd hh:mm")
                                Date close_approach_date_full,
                                long epoch_date_close_approach,
                                RelativeVelocity relative_velocity,
                                MissDistance miss_distance,
                                String orbiting_body) {

    public record RelativeVelocity(BigDecimal kilometers_per_second,
                                   BigDecimal kilometers_per_hour,
                                   BigDecimal miles_per_hour) {
    }

    public record MissDistance(BigDecimal astronomical,
                               BigDecimal lunar,
                               BigDecimal kilometers,
                               BigDecimal miles) {
    }
}
