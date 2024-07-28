package br.com.renatosantos.quarkussocial.rest;

import br.com.renatosantos.quarkussocial.domain.model.Follower;
import br.com.renatosantos.quarkussocial.domain.repository.FollowerRepository;
import br.com.renatosantos.quarkussocial.domain.repository.UserRepository;
import br.com.renatosantos.quarkussocial.handlers.exceptions.SocialMediaExceptions;
import br.com.renatosantos.quarkussocial.rest.dto.FollowerRequest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

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
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest followerRequest) {
        var user = userRepository.findById(userId);
        if (user == null)
            throw new NullPointerException("User with id " + userId + " not found");

        var follower = userRepository.findById(followerRequest.getFollowerId());
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
}
