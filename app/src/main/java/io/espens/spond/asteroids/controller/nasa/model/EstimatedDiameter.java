package io.espens.spond.asteroids.controller.nasa.model;

import java.math.BigDecimal;

public record EstimatedDiameter(EstimatedDiameterValue kilometers,
                                EstimatedDiameterValue meters,
                                EstimatedDiameterValue miles,
                                EstimatedDiameterValue feet) {
    public record EstimatedDiameterValue(BigDecimal estimated_diameter_min, BigDecimal estimated_diameter_max) {
    }
}
