package br.com.renatosantos.quarkussocial.rest;

import br.com.renatosantos.quarkussocial.domain.model.Follower;
import br.com.renatosantos.quarkussocial.domain.repository.FollowerRepository;
import br.com.renatosantos.quarkussocial.domain.repository.UserRepository;
import br.com.renatosantos.quarkussocial.handlers.exceptions.SocialMediaExceptions;
import br.com.renatosantos.quarkussocial.rest.dto.FollowerRequest;
import br.com.renatosantos.quarkussocial.rest.dto.FollowerResponse;
import br.com.renatosantos.quarkussocial.rest.dto.FollowersPerUserResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

/**
 * This class represents the RESTful resource for managing followers.
 * It handles HTTP requests related to following and unfollowing users.
 *
 * @author Renato
 * @since 1.0.0
 */

@Path("/users/{userId}/followers")
@Slf4j
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {
    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    /**
     * Constructor for the FollowerResource class.
     * It injects the required repositories using CDI.
     *
     * @param followerRepository The FollowerRepository instance
     * @param userRepository The UserRepository instance
     */

    @Inject
    public FollowerResource(FollowerRepository followerRepository,
                            UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    /**
     * Handles HTTP PUT requests to follow a user.
     * It checks if the user and follower exist, and if the user is not following the follower.
     * If all conditions are met, it creates a new Follower entity and persists it in the database.
     *
     * @param userId The ID of the user who wants to follow
     * @param followerRequest The request containing the ID of the follower
     * @return A Response with status 200 and a message indicating the successful follow
     * @throws NullPointerException If the user or follower does not exist
     * @throws SocialMediaExceptions.SelfFollowException If the user tries to follow themselves
     * @throws SocialMediaExceptions.AlreadyFollowedException If the user is already following the follower
     */
    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId,
                                FollowerRequest followerRequest) {
        var user = userRepository.findById(userId);
        if (user == null)
            throw new NullPointerException("User with id " + userId + " not found");

        var follower = userRepository.findById(followerRequest.getFollowerId());
        if (follower == null)
            throw new NullPointerException("Follower with id " + userId + " " +
                    "was not " +
                    "found");
        boolean follows = followerRepository.follows(follower, user);

        if (followerRepository.isSelfFollow(follower, user)) {
            throw new SocialMediaExceptions.SelfFollowException("User " + user.getName() + " cannot follow himself.");
        } else if (follows) {
            throw new SocialMediaExceptions.AlreadyFollowedException("User " + user.getName() + " is already being followed by user " + follower.getName());
        } else {
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);
            followerRepository.persist(entity);
            return Response.status(Response.Status.OK)
                    .entity("User " + user.getName() + " is now being followed by user " + follower.getName())
                    .build();
        }
    }

    /**
     * Handles HTTP GET requests to list the followers of a user.
     * It retrieves the list of followers from the database and returns them in a response.
     *
     * @param userId The ID of the user whose followers are to be listed
     * @return A Response with status 200 and the list of followers
     * @return A Response with status 404 If the user does not have any
     * followers
     * @throws NullPointerException If the user does not exist
     */
    @GET
public Response listFollowers(@PathParam("userId") Long userId){
    var user = userRepository.findById(userId);
    
    if (user == null) {
        throw new NullPointerException("User with id " + userId + " not found");
    }
    
    var list = followerRepository.findByUser(userId);
    
    if (list.isEmpty()) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("User " + user.getName() + " does not have any followers.")
                .build();
    }
    
    FollowersPerUserResponse responseObject =
            new FollowersPerUserResponse();
    responseObject.setFollowersCount(list.size());

    var followerList =
            list.stream().map(FollowerResponse::new).collect(Collectors.toList());
    responseObject.setContent(followerList);
    return Response.ok(responseObject).build();
}
    /**
     * Handles HTTP DELETE requests to unfollow a user.
     * It checks if the user and follower exist, and if the user is following the follower.
     * If all conditions are met, it deletes the Follower entity from the database.
     *
     * @param userId The ID of the user who wants to unfollow
     * @param followerId The ID of the follower to be unfollowed
     * @return A Response with status 200 and a message indicating the successful unfollow
     * @throws NullPointerException If the user or follower does not exist
     * @throws SocialMediaExceptions.NotFollowingException If the user is not following the follower
     * @throws IllegalStateException If the follower relationship does not exist in the database
     */
    @DELETE
    @Transactional
    public Response unfollowUser(@PathParam("userId") Long userId,
                                 @HeaderParam("followerId") Long followerId) {
        var user = userRepository.findById(userId);
        if (user == null)
            throw new NullPointerException("User with id " + userId + " not found");

        var follower = userRepository.findById(followerId);
        if (follower == null)
            throw new NullPointerException("Follower with id " + followerId + " not found");

        boolean follows = followerRepository.follows(follower, user);

        if (!follows) {
            throw new SocialMediaExceptions.NotFollowingException("User " + user.getName() + " is not following user " + follower.getName());
        } else {
            var entity = followerRepository.find("user.id = ?1 and follower.id = ?2", userId, followerId).firstResult();
            if (entity != null) {
                followerRepository.delete(entity);
                return Response.status(Response.Status.OK)
                        .entity("User " + user.getName() + " is no longer following user " + follower.getName())
                        .build();
            } else {
                throw new IllegalStateException("Follower relationship not found");
            }
        }
    }
}
