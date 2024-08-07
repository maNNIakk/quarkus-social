package br.com.renatosantos.quarkussocial.smoke;

import br.com.renatosantos.quarkussocial.domain.model.Follower;
import br.com.renatosantos.quarkussocial.domain.model.Post;
import br.com.renatosantos.quarkussocial.domain.model.User;
import br.com.renatosantos.quarkussocial.domain.repository.FollowerRepository;
import br.com.renatosantos.quarkussocial.domain.repository.PostRepository;
import br.com.renatosantos.quarkussocial.domain.repository.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.AssertTrue;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostResourceTest {


    private UserRepository userRepository;
    private FollowerRepository followerRepository;
    private PostRepository postRepository;
    private Long userId;
    private Long followerId;

    public PostResourceTest(UserRepository userRepository,
                            FollowerRepository followerRepository,
                            PostRepository postRepository){
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    @BeforeEach
    public void setup(){
        var user = new User();
        var follower = new User();

        user.setName("test_user");
        user.setAge(30);
        userRepository.persist(user);

        follower.setName("test_follower");
        follower.setAge(25);
        userRepository.persist(follower);

        userId = user.getId();
        followerId = follower.getId();

        var entity = new Follower();
        entity.setUser(user);
        entity.setFollower(follower);
        followerRepository.persist(entity);
    }

    @Test
    @Order(0)
    public void testCreatePost(){
        Post post = new Post();


        post.setText("test_post " + LocalDateTime.now().withNano(0));

        Response response = given().contentType(MediaType.APPLICATION_JSON)
                 .body(post)
                 .when().post("/users/" + userId + "/posts")
                 .then()
                 .statusCode(201)
                 .extract().response();
        assertTrue(response.asString().contains(post.getText()));
        assertEquals(201,response.statusCode());

    }

}