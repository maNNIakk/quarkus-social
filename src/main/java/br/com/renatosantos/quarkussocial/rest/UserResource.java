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

/**
 * UserResource class handles HTTP requests related to user management in the Quarkus Social application.
 * It provides endpoints for creating, reading, updating, and deleting user records.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserRepository userRepository;

    /**
     * Constructor for UserResource class.
     *
     * @param userRepository The UserRepository instance to interact with the user data.
     */
    @Inject
    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user record using the provided CreateUserRequest object.
     *
     * @param newUser The CreateUserRequest object containing the user details.
     * @return HTTP 201 Created with the created user object in the response body.
     */
    @POST
    @Transactional
    public Response createUser(@Valid CreateUserRequest newUser) {
        User user = new User();
        user.setAge(newUser.getAge());
        user.setName(newUser.getName());
        userRepository.persist(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    /**
     * Retrieves a list of all user records, with optional pagination support.
     *
     * @param page The page number to retrieve. Default is 0.
     * @param size The number of records per page. Default is 15.
     * @return HTTP 200 OK with a list of user objects in the response body.
     */
    @GET
    public Response listAllUsers(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        int pageIndex = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 15;

        PanacheQuery<User> query =
                userRepository.find("ORDER BY id ASC").page(pageIndex, pageSize);
        return Response.ok(query.list()).build();
    }

    /**
     * Updates an existing user record with the provided CreateUserRequest object.
     *
     * @param id The unique identifier of the user to update.
     * @param updatedUser The CreateUserRequest object containing the updated user details.
     * @return HTTP 200 OK with the updated user object in the response body.
     *         HTTP 404 Not Found if the user with the specified id does not exist.
     */
    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, @Valid CreateUserRequest updatedUser) {
        User user = userRepository.findById(id);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        user.setName(updatedUser.getName());
        user.setAge(updatedUser.getAge());

        userRepository.persist(user);
        return Response.ok(user).build();
    }

    /**
     * Deletes the user record with the specified id.
     *
     * @param id The unique identifier of the user to delete.
     * @return HTTP 204 No Content if the user with the specified id is deleted successfully.
     *         HTTP 404 Not Found if the user with the specified id does not exist.
     *         HTTP 500 Internal Server Error if an unexpected error occurs during deletion.
     */
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