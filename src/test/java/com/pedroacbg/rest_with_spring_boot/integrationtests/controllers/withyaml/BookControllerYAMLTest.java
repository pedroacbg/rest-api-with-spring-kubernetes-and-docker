package com.pedroacbg.rest_with_spring_boot.integrationtests.controllers.withyaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.pedroacbg.rest_with_spring_boot.config.TestsConfigs;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.BookDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.wrapper.xml_and_yaml.PagedModelBook;
import com.pedroacbg.rest_with_spring_boot.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerYAMLTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;
    private static YAMLMapper yamlMapper;
    private static BookDTO book;
    private static String yamlPerson;

    @BeforeAll
    static void setUp() throws JsonProcessingException {
        yamlMapper = new YAMLMapper();
        yamlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        book = new BookDTO();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockBook();
        yamlPerson = yamlMapper.writeValueAsString(book);
        requestSpecification = new RequestSpecBuilder().addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/books/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(requestSpecification)
                .config(config.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                .contentType("application/yaml")
                .accept("application/yaml")
                .body(yamlPerson)
                .when().post().then().statusCode(200)
                .contentType("application/yaml")
                .extract().body().asString();

        BookDTO createdBook = yamlMapper.readValue(content, BookDTO.class);
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
        yamlPerson = yamlMapper.writeValueAsString(book);

        var content = given(requestSpecification)
                .config(config.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                .contentType("application/yaml")
                .accept("application/yaml")
                .body(yamlPerson)
                .when().put().then().statusCode(200)
                .contentType("application/yaml")
                .extract().body().asString();

        BookDTO createdBook = yamlMapper.readValue(content, BookDTO.class);
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
                .config(config.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                .contentType("application/yaml")
                .accept("application/yaml")
                .pathParam("id", book.getId())
                .when().get("{id}").then().statusCode(200)
                .contentType("application/yaml")
                .extract().body().asString();

        BookDTO createdBook = yamlMapper.readValue(content, BookDTO.class);
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
                .config(config.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                .contentType("application/yaml")
                .accept("application/yaml")
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when().get().then().statusCode(200)
                .contentType("application/yaml")
                .extract().body().asString();

        PagedModelBook wrapper = yamlMapper.readValue(content, PagedModelBook.class);
        List<BookDTO> books = wrapper.getContent();

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