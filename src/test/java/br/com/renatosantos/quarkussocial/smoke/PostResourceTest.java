package br.com.renatosantos.quarkussocial.smoke;

import br.com.renatosantos.quarkussocial.domain.model.Follower;
import br.com.renatosantos.quarkussocial.domain.model.Post;
import br.com.renatosantos.quarkussocial.domain.model.User;
import br.com.renatosantos.quarkussocial.domain.repository.FollowerRepository;
import br.com.renatosantos.quarkussocial.domain.repository.PostRepository;
import br.com.renatosantos.quarkussocial.domain.repository.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
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
    private User user = new User();
    private User follower = new User();
    private Follower entity = new Follower();
    private Post post = new Post();


    public PostResourceTest(UserRepository userRepository,
                            FollowerRepository followerRepository,PostRepository postRepository
                            ){
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    @BeforeEach
    public void setup(){
        user.setName("test_user");
        user.setAge(30);
        userRepository.persist(user);

        follower.setName("test_follower");
        follower.setAge(25);
        userRepository.persist(follower);

        userId = user.getId();
        followerId = follower.getId();

        entity.setUser(user);
        entity.setFollower(follower);
        followerRepository.persist(entity);


        post.setUser(user);
        post.setDateTime(LocalDateTime.now().withNano(0));
        post.setText("test_listUserPostsTest");

        postRepository.persist(post);
    }

    @Test
    @Order(0)
    @DisplayName("Create Post Successfully")
    public void createTestPost(){


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

    @Test
    @Order(1)
    @DisplayName("Follower Listing User Followed Posts Successfully (having " +
            "posts)")
    public void listUserPostsTest(){

        System.out.println("Sending request to /users/" + userId + "/posts");

        Header header = new Header("followerId", String.valueOf(followerId));
            Response response =
                    given().contentType(MediaType.APPLICATION_JSON).header(header).when().get(
                            "/users/" + userId +
                                    "/posts").then().statusCode(200).extract().response();
        assertEquals(200,response.statusCode());
        assertTrue(response.asString().contains(post.getText()));
        System.out.println("Received response: " + response.asString());

    }
}