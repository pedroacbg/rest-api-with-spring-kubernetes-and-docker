package com.pedroacbg.rest_with_spring_boot.integrationtests.controllers.withxml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.pedroacbg.rest_with_spring_boot.config.TestsConfigs;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.PersonDTO;
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
class PersonControllerXMLTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;
    private static XmlMapper xmlMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        person = new PersonDTO();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();
        requestSpecification = new RequestSpecBuilder().addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(requestSpecification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(person)
                .when().post().then().statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract().body().asString();

        PersonDTO createdPerson = xmlMapper.readValue(content, PersonDTO.class);
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
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(person)
                .when().put().then().statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract().body().asString();

        PersonDTO createdPerson = xmlMapper.readValue(content, PersonDTO.class);
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
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", person.getId())
                .when().get("{id}").then().statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract().body().asString();

        PersonDTO createdPerson = xmlMapper.readValue(content, PersonDTO.class);
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
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", person.getId())
                .when().patch("{id}").then().statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract().body().asString();

        PersonDTO createdPerson = xmlMapper.readValue(content, PersonDTO.class);
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
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .when().get().then().statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract().body().asString();

        List<PersonDTO> people = xmlMapper.readValue(content, new TypeReference<List<PersonDTO>>() {});

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

        PersonDTO personSeven = people.get(5);

        assertNotNull(personSeven.getId());
        assertTrue(personSeven.getId() > 0);

        assertEquals("Bucewtao", personSeven.getFirstName());
        assertEquals("Sérgio", personSeven.getLastName());
        assertEquals("Xique-Xique - Bahia - Noruega", personSeven.getAddress());
        assertEquals("Female", personSeven.getGender());
        assertTrue(personSeven.getEnabled());

    }

    private void mockPerson() {
        person.setFirstName("Logerno");
        person.setLastName("Turvaniso");
        person.setAddress("Xique Xique - Norway");
        person.setGender("Male");
        person.setEnabled(true);
    }

}