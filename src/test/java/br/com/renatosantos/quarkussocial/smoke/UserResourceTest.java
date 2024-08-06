package br.com.renatosantos.quarkussocial.smoke;

import br.com.renatosantos.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserResourceTest {

    @Test
    @DisplayName("Should create an user successfully")
    public void createUserTest(){
        var user = new CreateUserRequest();
        user.setName("Fulaninho");
        user.setAge(20);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then()
                .extract()
                .response();
        assertEquals(201,response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));

    }

    @Test
    @DisplayName("Should return error when json is not valid")
    public void createUserValidationErrorTest(){
        var user = new CreateUserRequest();
        user.setName(null);
        user.setAge(null);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then()
                .extract()
                .response();

        boolean expectedViolationFound = false;
        List<Map<String,Object>> violations =
                JsonPath.from(response.asString()).getList("violations");
        if(violations != null){

        for (Map<String,Object> violation : violations) {
            String field = (String) violation.get("field");
            String message = (String) violation.get("message");
            if ("name".contains(field) && "Name is required".equals(message)){
                expectedViolationFound = true;
            assertTrue(expectedViolationFound, "Expected violation found for 'name' with message 'Name is required'");
            assertEquals(400,response.statusCode());
            break;
        }}


    }
        else {
            fail("Violation not found for 'name' with message 'Name is " +
                    "required'");

        }
}}