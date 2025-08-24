package com.pedroacbg.rest_with_spring_boot.integrationtests.controllers.withxml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.pedroacbg.rest_with_spring_boot.config.TestsConfigs;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.AccountCredentialsDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.BookDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.TokenDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.wrapper.xml_and_yaml.PagedModelBook;
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
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerXMLTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;
    private static XmlMapper xmlMapper;
    private static BookDTO book;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        book = new BookDTO();
        tokenDTO =  new TokenDTO();
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
    void createTest() throws JsonProcessingException {
        mockBook();
        requestSpecification = new RequestSpecBuilder().addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_LOCALHOST)
                .addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/books/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(book)
                .when().post().then().statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract().body().asString();

        BookDTO createdBook = xmlMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Reinaldo", createdBook.getAuthor());
        assertEquals("O livro que vai mudar sua vida ou não", createdBook.getTitle());
        assertEquals(20D, createdBook.getPrice());
        assertEquals(new Date(1511968513636L), createdBook.getLaunchDate());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        book.setPrice(21D);

        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(book)
                .when().put().then().statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract().body().asString();

        BookDTO createdBook = xmlMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Reinaldo", createdBook.getAuthor());
        assertEquals("O livro que vai mudar sua vida ou não", createdBook.getTitle());
        assertEquals(21D, createdBook.getPrice());
        assertEquals(new Date(1511968513636L), createdBook.getLaunchDate());
    }

    @Test
    @Order(3)
    void findByidTest() throws JsonProcessingException {
        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", book.getId())
                .when().get("{id}").then().statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract().body().asString();

        BookDTO createdBook = xmlMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Reinaldo", createdBook.getAuthor());
        assertEquals("O livro que vai mudar sua vida ou não", createdBook.getTitle());
        assertEquals(21D, createdBook.getPrice());
    }

    @Test
    @Order(4)
    void deleteTest() throws JsonProcessingException {
        given(requestSpecification)
                .pathParam("id", book.getId())
                .when().delete("{id}").then().statusCode(204);
    }

    @Test
    @Order(5)
    void findAllTest() throws JsonProcessingException {
        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParams("page", 9, "size", 12, "direction", "asc")
                .when().get().then().statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract().body().asString();

        PagedModelBook wrapper = xmlMapper.readValue(content, PagedModelBook.class);
        List<BookDTO> books = wrapper.getContent();

        BookDTO bookOne = books.get(0);

        assertNotNull(bookOne.getId());
        assertTrue(bookOne.getId() > 0);

        assertEquals("James Shore e Shane Warden", bookOne.getAuthor());
        assertEquals("The Art of Agile Development", bookOne.getTitle());
        assertEquals(97.21, bookOne.getPrice());

        BookDTO bookFour = books.get(3);

        assertNotNull(bookFour.getId());
        assertTrue(bookFour.getId() > 0);

        assertEquals("James Shore e Shane Warden", bookOne.getAuthor());
        assertEquals("The Art of Agile Development", bookOne.getTitle());
        assertEquals(97.21, bookOne.getPrice());

        BookDTO bookSix = books.get(5);

        assertNotNull(bookSix.getId());
        assertTrue(bookSix.getId() > 0);

        assertEquals("Donald E. Knuth", bookSix.getAuthor());
        assertEquals("The Art of Computer Programming, Volume 1: Fundamental Algorithms", bookSix.getTitle());
        assertEquals(52.11, bookSix.getPrice());

    }

    private void mockBook() {
        book.setAuthor("Reinaldo");
        book.setTitle("O livro que vai mudar sua vida ou não");
        book.setPrice(20D);
        book.setLaunchDate(new Date(1511968513636L));
    }

}