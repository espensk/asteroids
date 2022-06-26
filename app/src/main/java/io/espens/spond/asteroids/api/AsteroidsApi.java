package io.espens.spond.asteroids.api;

import io.espens.spond.asteroids.App;
import io.espens.spond.asteroids.api.model.Asteroid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("/asteroids")
public class AsteroidsApi {

    private static final Logger LOG = LoggerFactory.getLogger(AsteroidsApi.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @GET
    @Path("close")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Asteroid> close(@QueryParam("start_date") String start,
                                @QueryParam("end_date") String end,
                                @QueryParam("limit") @DefaultValue("10") int limit) {
        Date startDate = null;
        try {
            startDate = DATE_FORMAT.parse(start);
            Date endDate = DATE_FORMAT.parse(end);

            LOG.info("Querying {} asteroids between {} and {}", limit, startDate, endDate);
            return List.of(getBiggest(2021));
        } catch (ParseException e) {
            throw new BadRequestException("Failed to parse input date");
        }
    }


    @GET
    @Path("biggest")
    @Produces(MediaType.APPLICATION_JSON)
    public Asteroid getBiggest(@QueryParam("year") Integer year) {
        LOG.info("Get the biggest asteroid passing earth in year {}", year);
        try {
            return new Asteroid("dummy",
                    "espens-asterodi",
                    new BigDecimal("32.312312"),
                    new URL("https://nasa.gov/jepsen"));
        }
        catch (MalformedURLException e) {
            throw new WebApplicationException("Poorly configured url", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}
