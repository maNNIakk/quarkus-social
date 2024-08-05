package br.com.renatosantos.quarkussocial.rest;

import br.com.renatosantos.quarkussocial.domain.model.Post;
import br.com.renatosantos.quarkussocial.domain.model.User;
import br.com.renatosantos.quarkussocial.domain.repository.FollowerRepository;
import br.com.renatosantos.quarkussocial.domain.repository.PostRepository;
import br.com.renatosantos.quarkussocial.domain.repository.UserRepository;
import br.com.renatosantos.quarkussocial.handlers.exceptions.SocialMediaExceptions;
import br.com.renatosantos.quarkussocial.rest.dto.CreatePostRequest;
import br.com.renatosantos.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@Path("/users/{userId}/posts")
@Slf4j
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class PostResource {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowerRepository followerRepository;

    @Inject
    public PostResource(UserRepository userRepository,
                        PostRepository postRepository, FollowerRepository followerRepository){

        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId,
                             @Valid CreatePostRequest postRequest) {
        try {
            User user = userRepository.findById(userId);
            if (user == null) throw new NullPointerException("User with id " + userId + " not found");
            Post post = new Post();
            post.setText(postRequest.getText());
            post.setUser(user);
            postRepository.persist(post);

            return Response.status(Response.Status.CREATED)
                    .entity("Post saved for user " + user.getName())
                    .build();
        } catch (NullPointerException e) {
            log.error("Caught an exception: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    public Response listUserPosts(@PathParam("userId") Long userId,
                                  @HeaderParam("followerId") Long followerId ){
        try{
            User follower = userRepository.findById(followerId);
            if(follower == null) throw new NullPointerException("User with id" +
                    " " + userId + " not found, couldn't " +
                    "list posts");
            User user = userRepository.findById(userId);
            if(followerRepository.follows(follower,user)){

            if(user == null) throw new NullPointerException("User with id " + userId + " not found, couldn't " +
                    "list posts");
            PanacheQuery<Post> query = postRepository.find("user", Sort.by(
                    "dateTime", Sort.Direction.Descending), user);

            var list = query.list();
            if (list.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User " + user.getName() + " does not have any posts.")
                        .build();
            } else {
                var postResponseList =
                        list.stream().map(PostResponse::fromEntity).collect(Collectors.toList());

                return Response.ok(postResponseList).build();
            }
            } else { throw new SocialMediaExceptions.NotFollowingException(
                    "User " + user.getName() + " is not following user " + follower.getName() + " and cannot see it's posts");

            }
        } catch (NullPointerException e){
            log.error("Caught an exception: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }


}
