package com.pedroacbg.rest_with_spring_boot.integrationtests.controllers.cors.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroacbg.rest_with_spring_boot.config.TestsConfigs;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.AccountCredentialsDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.BookDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.TokenDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerCorsTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;
    private static ObjectMapper objectMapper;
    private static BookDTO book;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        book = new BookDTO();
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(0)
    void signIn() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("jeraldo", "admin123");

        tokenDTO = given()
                .basePath("/auth/signin")
                .port(TestsConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().as(TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockBook();
        requestSpecification = new RequestSpecBuilder().addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_GULERONE)
                .addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/books/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .when().post().then().statusCode(200)
                .extract().body().asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getLaunchDate());

        assertTrue(createdBook.getId() > 0);

        assertEquals("Reinaldo", createdBook.getAuthor());
        assertEquals("O livro que vai mudar sua vida ou não", createdBook.getTitle());
        assertEquals(20D, createdBook.getPrice());
        assertEquals(new Date(1511968513636L), createdBook.getLaunchDate());
    }

    @Test
    @Order(2)
    void createWithWrongOrigin() throws JsonProcessingException {
        requestSpecification = new RequestSpecBuilder().addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_GOOGLE)
                .addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/books/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .when().post().then().statusCode(403)
                .extract().body().asString();

        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    void findByid() throws JsonProcessingException {
        requestSpecification = new RequestSpecBuilder().addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_LOCALHOST)
                .addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/books/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", book.getId())
                .when().get("{id}").then().statusCode(200)
                .extract().body().asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getLaunchDate());

        assertTrue(createdBook.getId() > 0);

        assertEquals("Reinaldo", createdBook.getAuthor());
        assertEquals("O livro que vai mudar sua vida ou não", createdBook.getTitle());
        assertEquals(20D, createdBook.getPrice());
    }

    @Test
    @Order(4)
    void findByidWithWrongOrigin() throws JsonProcessingException {
        requestSpecification = new RequestSpecBuilder().addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_GOOGLE)
                .addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/books/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", book.getId())
                .when().get("{id}").then().statusCode(403)
                .extract().body().asString();

        assertEquals("Invalid CORS request", content);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findAll() {
    }

    private void mockBook() {
        book.setAuthor("Reinaldo");
        book.setTitle("O livro que vai mudar sua vida ou não");
        book.setPrice(20D);
        book.setLaunchDate(new Date(1511968513636L));
    }

}