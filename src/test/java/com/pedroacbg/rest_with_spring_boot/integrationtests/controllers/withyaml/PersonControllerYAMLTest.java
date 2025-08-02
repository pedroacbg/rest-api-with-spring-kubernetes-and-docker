package com.pedroacbg.rest_with_spring_boot.integrationtests.controllers.withyaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.pedroacbg.rest_with_spring_boot.config.TestsConfigs;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.PersonDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.wrapper.xml.PagedModelPerson;
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

import java.util.List;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYAMLTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;
    private static YAMLMapper yamlMapper;
    private static PersonDTO person;
    private static String yamlPerson;

    @BeforeAll
    static void setUp() throws JsonProcessingException {
        yamlMapper = new YAMLMapper();
        yamlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        person = new PersonDTO();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();
        yamlPerson = yamlMapper.writeValueAsString(person);
        requestSpecification = new RequestSpecBuilder().addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/person/v1")
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

        PersonDTO createdPerson = yamlMapper.readValue(content, PersonDTO.class);
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
        yamlPerson = yamlMapper.writeValueAsString(person);

        var content = given(requestSpecification)
                .config(config.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                .contentType("application/yaml")
                .accept("application/yaml")
                .body(yamlPerson)
                .when().put().then().statusCode(200)
                .contentType("application/yaml")
                .extract().body().asString();

        PersonDTO createdPerson = yamlMapper.readValue(content, PersonDTO.class);
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
                .config(config.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                .contentType("application/yaml")
                .accept("application/yaml")
                .pathParam("id", person.getId())
                .when().get("{id}").then().statusCode(200)
                .contentType("application/yaml")
                .extract().body().asString();

        PersonDTO createdPerson = yamlMapper.readValue(content, PersonDTO.class);
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
                .config(config.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                .contentType("application/yaml")
                .accept("application/yaml")
                .pathParam("id", person.getId())
                .when().patch("{id}").then().statusCode(200)
                .contentType("application/yaml")
                .extract().body().asString();

        PersonDTO createdPerson = yamlMapper.readValue(content, PersonDTO.class);
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
                .config(config.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                .contentType("application/yaml")
                .accept("application/yaml")
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when().get().then().statusCode(200)
                .contentType("application/yaml")
                .extract().body().asString();

        PagedModelPerson wrapper = yamlMapper.readValue(content, PagedModelPerson.class);
        List<PersonDTO> people = wrapper.getContent();

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

        PersonDTO personSeven = people.get(5);

        assertNotNull(personSeven.getId());
        assertTrue(personSeven.getId() > 0);

        assertEquals("Anne-corinne", personSeven.getFirstName());
        assertEquals("Trevan", personSeven.getLastName());
        assertEquals("PO Box 43644", personSeven.getAddress());
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