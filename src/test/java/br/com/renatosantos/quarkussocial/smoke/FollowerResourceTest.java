package br.com.renatosantos.quarkussocial.smoke;

import br.com.renatosantos.quarkussocial.domain.model.Follower;
import br.com.renatosantos.quarkussocial.domain.model.User;
import br.com.renatosantos.quarkussocial.domain.repository.FollowerRepository;
import br.com.renatosantos.quarkussocial.domain.repository.UserRepository;
import br.com.renatosantos.quarkussocial.rest.dto.FollowerRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FollowerResourceTest {

    private Long userId;
    private Long follower1Id;
    private Long follower2Id;
    private User user = new User();
    private User follower1 = new User();
    private User follower2 = new User();
    private Follower entity = new Follower();

    private UserRepository userRepository;
    private FollowerRepository followerRepository;

    public FollowerResourceTest(UserRepository userRepository,
                                FollowerRepository followerRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @BeforeEach
    public void setup() {
        user.setName("test_user");
        user.setAge(30);
        userRepository.persist(user);
        userId = user.getId();

        follower1.setName("test_followUser");
        follower1.setAge(25);
        userRepository.persist(follower1);
        follower1Id = follower1.getId();

        follower2.setName("test_otherTests");
        follower2.setAge(35);
        userRepository.persist(follower2);
        follower2Id = follower2.getId();

        entity.setUser(user);
        entity.setFollower(follower2);
        followerRepository.persist(entity);

    }

    @Test
    @Order(0)
    @Transactional
    public void followUser() {
        FollowerRequest followerRequest = new FollowerRequest();
        followerRequest.setFollowerId(follower1Id);


        System.out.println("User id : " + userId + " follower id : " + follower1Id);
        Response response =
                given().contentType(MediaType.APPLICATION_JSON).body(followerRequest).when().put(
                        "/users/" + userId +
                        "/followers").then()
                        .statusCode(200)
                        .extract().response();
        System.out.println(response.asString());
        assertEquals(200,response.statusCode());

    }

    @Test
    @Transactional
    @Order(1)
    public void listFollowers() {

        Response response = given().when().get("/users/" + userId + "/followers").then()
                .statusCode(200)
                .extract().response();
        System.out.println(response.asString());
        assertEquals(200,response.statusCode());
    }

    @Test
    @Transactional
    @Order(2)
    public void unfollowUser() {

        Header header = new Header("followerId", String.valueOf(follower2Id));

        Response response =
                given().contentType(MediaType.APPLICATION_JSON).header(header).when().delete(
                        "/users/" + userId +
                        "/followers").then()
                        .statusCode(200)
                        .extract().response();
        System.out.println(response.asString());
        assertEquals(200,response.statusCode());
    }
}
