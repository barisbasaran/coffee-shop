package io.baris.coffeeshop.homepage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Home resource to serve homepage
 */
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_HTML)
@Slf4j
@RequiredArgsConstructor
public class HomepageResource {

    @GET
    public String homepage() {
        return "Welcome to the Coffee Shop";
    }
}
