package br.com.renatosantos.quarkussocial.smoke;

import br.com.renatosantos.quarkussocial.rest.UserResource;
import br.com.renatosantos.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestHTTPEndpoint(UserResource.class)
class UserResourceTest {


    @Test
    @Order(0)
    @DisplayName("Should create an user successfully")
    public void createUserTest() {
        var user = new CreateUserRequest();
        user.setName("Fulaninho");
        user.setAge(20);

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .extract()
                .response();
        assertEquals(201,response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
        System.out.println(user.getAge());
        System.out.println(user.getName());
    }

    @Test
    @Order(1)
    @DisplayName("Should thrown Age and Name violations")
    public void createUserValidationErrorTest(){
        var user = new CreateUserRequest();
        user.setName(null);
        user.setAge(null);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post()
                .then()
                .extract()
                .response();

        boolean nameViolationFound = false;
        boolean ageViolationFound = false;
        List<Map<String,Object>> violations =
                JsonPath.from(response.asString()).getList("violations");
        if(violations != null){

            for (Map<String,Object> violation : violations) {
                String field = (String) violation.get("field");
                String message = (String) violation.get("message");
                if (field.contains("name") && message.equals("Name is required")){
                    nameViolationFound = true;
                }
                else if (field.contains("age") && message.equals("Age is required")){
                    ageViolationFound = true;

                }
                assertEquals(400,response.statusCode());
            }
            assertTrue(nameViolationFound, "Expected name violation found");
            assertTrue(ageViolationFound, "Expected age violation found");

        }
    }
    @Test
    @Order(2)
    @DisplayName("Should return a list of users")
    public void getUsersTest() {
        Response response = given()
                .when()
                .get()
                .then()
                .extract()
                .response();
        System.out.println(response);
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());

        List<Map<String, Object>> users = response.jsonPath().getList("");
        assertNotNull(users);
        assertTrue(users.size() > 0, "Expected at least one user in the list");
        System.out.println();

        boolean found = false;

        for (Map<String,Object> user : users){
            if(user.get("name").toString().equals("Fulaninho")){
                System.out.println(user.get("name"));
                System.out.println(user.get("age"));
                assertEquals("Fulaninho", user.get("name"));
                assertEquals(20, user.get("age"));
                found = true;
                break;
            }

        }
        assertTrue(found, "Expected user Fulaninho to be found");
    }
}