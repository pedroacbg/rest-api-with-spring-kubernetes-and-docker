package com.pedroacbg.rest_with_spring_boot.integrationtests.controllers.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroacbg.rest_with_spring_boot.config.TestsConfigs;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.BookDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.wrapper.json.WrapperBookDTO;
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
class BookControllerJSONTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;
    private static ObjectMapper objectMapper;
    private static BookDTO book;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        book = new BookDTO();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockBook();
        requestSpecification = new RequestSpecBuilder().addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/books/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .when().post().then().statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Reinaldo", createdBook.getAuthor());
        assertEquals("O livro que vai mudar sua vida ou não", createdBook.getTitle());
        assertEquals(20D, createdBook.getPrice());
        assertEquals(new Date(1511968513636L), createdBook.getLaunch_date());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        book.setPrice(21D);

        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .when().put().then().statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Reinaldo", createdBook.getAuthor());
        assertEquals("O livro que vai mudar sua vida ou não", createdBook.getTitle());
        assertEquals(21D, createdBook.getPrice());
        assertEquals(new Date(1511968513636L), createdBook.getLaunch_date());
    }

    @Test
    @Order(3)
    void findByidTest() throws JsonProcessingException {
        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", book.getId())
                .when().get("{id}").then().statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);

        assertEquals("Reinaldo", createdBook.getAuthor());
        assertEquals("O livro que vai mudar sua vida ou não", createdBook.getTitle());
        assertEquals(21D, createdBook.getPrice());
        assertEquals(new Date(1511920800000L), createdBook.getLaunch_date());
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
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when().get().then().statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        WrapperBookDTO wrapper = objectMapper.readValue(content, WrapperBookDTO.class);
        List<BookDTO> books = wrapper.getEmbedded().getBooks();

        BookDTO bookOne = books.get(0);

        assertNotNull(bookOne.getId());
        assertTrue(bookOne.getId() > 0);

        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", bookOne.getAuthor());
        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", bookOne.getTitle());
        assertEquals(54D, bookOne.getPrice());
        assertEquals(new Date(1510020000000L), bookOne.getLaunch_date());

        BookDTO bookFour = books.get(3);

        assertNotNull(bookFour.getId());
        assertTrue(bookFour.getId() > 0);

        assertEquals("Ralph Johnson, Erich Gamma, John Vlissides e Richard Helm", bookFour.getAuthor());
        assertEquals("Design Patterns", bookFour.getTitle());
        assertEquals(45D, bookFour.getPrice());
        assertEquals(new Date(1511920800000L), bookFour.getLaunch_date());

        BookDTO bookSix = books.get(5);

        assertNotNull(bookSix.getId());
        assertTrue(bookSix.getId() > 0);

        assertEquals("Roger S. Pressman", bookSix.getAuthor());
        assertEquals("Engenharia de Software: uma abordagem profissional", bookSix.getTitle());
        assertEquals(56D, bookSix.getPrice());
        assertEquals(new Date(1510020000000L), bookSix.getLaunch_date());

    }

    private void mockBook() {
        book.setAuthor("Reinaldo");
        book.setTitle("O livro que vai mudar sua vida ou não");
        book.setPrice(20D);
        book.setLaunch_date(new Date(1511968513636L));
    }

}