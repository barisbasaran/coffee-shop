package io.baris.coffeeshop.cqrs.event;

import io.baris.coffeeshop.cqrs.event.EventManager;
import io.baris.coffeeshop.cqrs.event.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Serves event endpoints.json
 */
@Path("/events")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Slf4j
@RequiredArgsConstructor
public class EventResource {

    private final EventManager eventManager;

    @GET
    public List<Event> getAllEvents() {
        return eventManager.getAllEvents();
    }
}
