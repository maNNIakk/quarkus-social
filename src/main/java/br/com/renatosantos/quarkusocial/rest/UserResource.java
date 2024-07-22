package br.com.renatosantos.quarkusocial.rest;

import br.com.renatosantos.quarkusocial.rest.dto.User;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserResource {

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User userRequest){
        return Response.ok(userRequest).build();
    }
    @GET
    public Response listAllUsers(){
        return Response.ok().build();
    }
}
