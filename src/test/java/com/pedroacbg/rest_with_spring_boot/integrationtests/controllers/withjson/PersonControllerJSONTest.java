package com.pedroacbg.rest_with_spring_boot.integrationtests.controllers.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroacbg.rest_with_spring_boot.config.TestsConfigs;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.AccountCredentialsDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.PersonDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.TokenDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.wrapper.json.WrapperPersonDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJSONTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        person = new PersonDTO();
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
    void createTest() throws JsonProcessingException {
        mockPerson();
        requestSpecification = new RequestSpecBuilder().addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_LOCALHOST)
                .addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when().post().then().statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Logerno", createdPerson.getFirstName());
        assertEquals("Turvaniso", createdPerson.getLastName());
        assertEquals("Xique Xique - Norway", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        person.setLastName("Brevinaldo Sergulo");

        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when().put().then().statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Logerno", createdPerson.getFirstName());
        assertEquals("Brevinaldo Sergulo", createdPerson.getLastName());
        assertEquals("Xique Xique - Norway", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(3)
    void findByidTest() throws JsonProcessingException {
        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when().get("{id}").then().statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Logerno", createdPerson.getFirstName());
        assertEquals("Brevinaldo Sergulo", createdPerson.getLastName());
        assertEquals("Xique Xique - Norway", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {
        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when().patch("{id}").then().statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Logerno", createdPerson.getFirstName());
        assertEquals("Brevinaldo Sergulo", createdPerson.getLastName());
        assertEquals("Xique Xique - Norway", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void deleteTest() throws JsonProcessingException {
        given(requestSpecification)
                .pathParam("id", person.getId())
                .when().delete("{id}").then().statusCode(204);
    }

    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {
        var content = given(requestSpecification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when().get().then().statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();

        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Anabelle", personOne.getFirstName());
        assertEquals("Dykas", personOne.getLastName());
        assertEquals("PO Box 81494", personOne.getAddress());
        assertEquals("Female", personOne.getGender());
        assertFalse(personOne.getEnabled());

        PersonDTO personFour = people.get(3);

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Anna", personFour.getFirstName());
        assertEquals("Ashwin", personFour.getLastName());
        assertEquals("Suite 43", personFour.getAddress());
        assertEquals("Female", personFour.getGender());
        assertTrue(personFour.getEnabled());

        PersonDTO personSix = people.get(5);

        assertNotNull(personSix.getId());
        assertTrue(personSix.getId() > 0);

        assertEquals("Anne-corinne", personSix.getFirstName());
        assertEquals("Trevan", personSix.getLastName());
        assertEquals("PO Box 43644", personSix.getAddress());
        assertEquals("Female", personSix.getGender());
        assertTrue(personSix.getEnabled());

    }

    @Test
    @Order(7)
    void findByName() throws JsonProcessingException {
        var content = given(requestSpecification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("firstName", "matheus")
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when().get("findByName/{firstName}").then().statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();

        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Matheus", personOne.getFirstName());
        assertEquals("Buceta", personOne.getLastName());
        assertEquals("Xique-Xique - Bahia - Noruega", personOne.getAddress());
        assertEquals("Female", personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTO personFour = people.get(3);

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Matheus", personFour.getFirstName());
        assertEquals("Sérgio", personFour.getLastName());
        assertEquals("Xique-Xique - Bahia - Noruega", personFour.getAddress());
        assertEquals("Female", personFour.getGender());
        assertTrue(personFour.getEnabled());

        PersonDTO personFive = people.get(4);

        assertNotNull(personFive.getId());
        assertTrue(personFive.getId() > 0);

        assertEquals("Matheus", personFive.getFirstName());
        assertEquals("Sérgio", personFive.getLastName());
        assertEquals("Xique-Xique - Bahia - Noruega", personFive.getAddress());
        assertEquals("Female", personFive.getGender());
        assertTrue(personFive.getEnabled());

    }

    private void mockPerson() {
        person.setFirstName("Logerno");
        person.setLastName("Turvaniso");
        person.setAddress("Xique Xique - Norway");
        person.setGender("Male");
        person.setEnabled(true);
    }

}