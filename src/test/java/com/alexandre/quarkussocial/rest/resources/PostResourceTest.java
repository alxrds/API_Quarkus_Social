package com.alexandre.quarkussocial.rest.resources;

import com.alexandre.quarkussocial.rest.dtos.CreatePostRequest;
import com.alexandre.quarkussocial.rest.models.Follower;
import com.alexandre.quarkussocial.rest.models.Post;
import com.alexandre.quarkussocial.rest.models.User;
import com.alexandre.quarkussocial.rest.repositories.FollowerRepository;
import com.alexandre.quarkussocial.rest.repositories.PostRepository;
import com.alexandre.quarkussocial.rest.repositories.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;
    @Inject
    PostRepository postRepository;

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUP(){
        var user = new User();
        user.setName("Usuário teste 1");
        user.setAge(30);
        userRepository.persist(user);
        userId = user.getId();

        Post post = new Post();
        post.setText("Post teste 1");
        post.setUser(user);
        postRepository.persist(post);

        var userNotFollower = new User();
        userNotFollower.setName("Usuário Teste 2");
        userNotFollower.setAge(33);
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        var userFollower = new User();
        userFollower.setName("Usuário teste 3");
        userFollower.setAge(31);
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);

    }

    @Test
    @DisplayName("should create a post for a user")
    public void createPostTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Post teste 1");

        given()
            .contentType(ContentType.JSON)
            .body(postRequest)
            .pathParam("userId", userId)
        .when()
            .post()
        .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("should return 404 when trying to make a post for an inexistent user")
    public void postForAnInexistentUserTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Post teste 2");

        var inexistentUserId = 999;

        given()
            .contentType(ContentType.JSON)
            .body(postRequest)
            .pathParam("userId", inexistentUserId)
        .when()
            .post()
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("should return 404 when user doesn't exist")
    public void listPostUserNotFoundTest(){
        var inexistentUserId = 999;

        given()
            .pathParam("userId", inexistentUserId)
        .when()
            .get()
        .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("should return 400 when followerId header is not present")
    public void listPostFollowerHeaderNotSendTest(){
        given()
            .pathParam("userId", userId)
        .when()
            .get()
        .then()
            .statusCode(400)
            .body(Matchers.is("You forgot the header followerId"));
    }

    @Test
    @DisplayName("should return 400 when follower doesn't exist")
    public void listPostFollowerNotFoundTest(){

        var inexistentFollowerId = 999;

        given()
            .pathParam("userId", userId)
            .header("followerId", inexistentFollowerId)
        .when()
            .get()
        .then()
            .statusCode(400)
            .body(Matchers.is("Inexistent followerId"));
    }

    @Test
    @DisplayName("should return 403 when follower isn't a follower")
    public void listPostNotAFollower(){
        given()
            .pathParam("userId", userId)
            .header("followerId", userNotFollowerId)
        .when()
            .get()
        .then()
            .statusCode(403)
            .body(Matchers.is("You can't see these posts"));
    }

    @Test
    @DisplayName("should list posts")
    public void listPostsTest(){
        given()
            .pathParam("userId", userId)
            .header("followerId", userFollowerId)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("size()", Matchers.is(1));
    }
}