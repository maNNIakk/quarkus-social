package br.com.renatosantos.quarkussocial.rest;

import br.com.renatosantos.quarkussocial.domain.model.User;
import br.com.renatosantos.quarkussocial.domain.repository.UserRepository;
import br.com.renatosantos.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserRepository userRepository;

    @Inject
    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @POST
    @Transactional
    public Response createUser(@Valid CreateUserRequest newUser) {
             User user = new User();
             user.setAge(newUser.getAge());
             user.setName(newUser.getName());
             userRepository.persist(user);
            return Response.status(Response.Status.CREATED).entity(user).build();

    }

    @GET
    public Response listAllUsers(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        int pageIndex = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 15;

        PanacheQuery<User> query =
                userRepository.find("ORDER BY id ASC").page(pageIndex,
                pageSize);
        return Response.ok(query.list()).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id,
                               @Valid CreateUserRequest updatedUser) {
        User user = userRepository.findById(id);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        user.setName(updatedUser.getName());
        user.setAge(updatedUser.getAge());

        userRepository.persist(user);
        return Response.ok(user).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            if (userRepository.deleteById(id)) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User with id " + id + " not found.")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred. Please try again later.")
                    .build();
        }
    }
}
