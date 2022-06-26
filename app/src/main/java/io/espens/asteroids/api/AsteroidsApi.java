package io.espens.asteroids.api;

import io.espens.asteroids.api.model.Asteroid;
import io.espens.asteroids.controller.AsteroidsException;
import io.espens.asteroids.controller.AsteroidsProvider;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/asteroids")
public class AsteroidsApi {

    private static final Logger LOG = LoggerFactory.getLogger(AsteroidsApi.class);

    private final AsteroidsProvider asteroidsProvider;

    @Inject
    public AsteroidsApi(@Named("caching") AsteroidsProvider provider) {
        this.asteroidsProvider = provider;
    }

    @GET
    @Path("close")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Asteroid> close(@QueryParam("start_date") String start,
                                @QueryParam("end_date") String end,
                                @QueryParam("limit") @DefaultValue("10") int limit) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            return asteroidsProvider.getAsteroids(startDate, endDate)
                    .stream()
                    .sorted(Comparator.comparing(Asteroid::distance))
                    .limit(limit)
                    .collect(Collectors.toList());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Failed to parse start or end dates", e);
        } catch (AsteroidsException e) {
            LOG.warn("Failed to get asteroids: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to retrieve asteroids from remove", e);
        }
    }


    @GET
    @Path("biggest")
    @Produces(MediaType.APPLICATION_JSON)
    public Asteroid getBiggest(@QueryParam("year") Integer year) {
        LOG.info("Get the biggest asteroid passing earth in year {}", year);
        try {
            var start = LocalDate.of(year, 1,1);
            var end = LocalDate.of(year + 1, 1, 1);
            var all = asteroidsProvider.getAsteroids(start, end);
            return all.stream()
                    .max(Comparator.comparing(Asteroid::diameter))
                    .orElseThrow(() -> new AsteroidsException("No asteroids returned"));
        }
        catch(AsteroidsException e) {
            LOG.warn("Failed to get asteroids: {}", e.getMessage());
            throw new WebApplicationException("Failed to get asteroids", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}
