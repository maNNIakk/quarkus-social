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
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Slf4j
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {
    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    @Inject
    public FollowerResource(FollowerRepository followerRepository,
                            UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

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
    @DELETE
    @Transactional
    public Response unfollowUser(@PathParam("userId") Long userId,
                                 @QueryParam("followerId") Long followerId) {
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
