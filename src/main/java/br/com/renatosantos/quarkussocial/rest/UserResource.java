package br.com.renatosantos.quarkussocial.rest;


import br.com.renatosantos.quarkussocial.domain.model.User;
import br.com.renatosantos.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Transactional
    public Response createUser(@Valid User newUser){
        try{
            newUser.persist();
            return Response.status(Response.Status.CREATED).entity(newUser).build();
        } catch (ConstraintViolationException e){
            return Response.status(400,"Algo errado aqui parsa").build();
        }
//        User user = new User();
//        user.setAge(userRequest.getAge());
//        user.setName(userRequest.getName());
//
//        user.persist();
//
//        return Response.ok(user).build();
    }
    @GET
    public Response listAllUsers(@QueryParam("page") Integer page, @QueryParam(
            "size") Integer size){
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 15;

        PanacheQuery<User> query = User.find("ORDER BY id ASC").page(pageNumber,
                pageSize);
        return Response.ok(query.list()).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest updatedUser){
        User user = User.findById(id);
        if(user ==null ){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        user.setName(updatedUser.getName());
        user.setAge(updatedUser.getAge());

        user.persist();
        return Response.ok(user).build();

    }


    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Long id){
        boolean deleted = User.deleteById(id);
        if (deleted){
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
