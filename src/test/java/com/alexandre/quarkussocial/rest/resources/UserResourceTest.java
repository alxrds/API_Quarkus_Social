package com.alexandre.quarkussocial.rest.resources;

import com.alexandre.quarkussocial.rest.dtos.CreateUserRequest;
import com.alexandre.quarkussocial.rest.dtos.ResponseError;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {

    @TestHTTPResource("/users")
    URL apiURL;

    @Test
    @DisplayName("sucesso! o usuário foi criado")
    @Order(1)
    public void createUserTest(){
        var user = new CreateUserRequest();
        user.setName("Usuário Teste");
        user.setAge(30);

        var response = given()
            .contentType(ContentType.JSON)
            .body(user)
        .when()
            .post(apiURL)
        .then()
            .extract().response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("sucesso! os campos foram validados")
    @Order(2)
    public void createUserValidationErrorTest(){
        var user = new CreateUserRequest();
        user.setName(null);
        user.setAge(null);

        var response = given()
            .contentType(ContentType.JSON)
            .body(user)
        .when()
            .post(apiURL)
        .then()
            .extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));

    }

    @Test
    @DisplayName("sucesso! usuarios listados")
    @Order(3)
    public void listAllUsersTest(){
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(apiURL)
        .then()
            .statusCode(200)
            .body("size()", Matchers.is(1));
    }

}