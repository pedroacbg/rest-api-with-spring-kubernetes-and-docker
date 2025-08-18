package com.pedroacbg.rest_with_spring_boot.integrationtests.controllers.withyaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.pedroacbg.rest_with_spring_boot.config.TestsConfigs;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.AccountCredentialsDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.TokenDTO;
import com.pedroacbg.rest_with_spring_boot.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerYAMLTest extends AbstractIntegrationTest {

    private static TokenDTO tokenDTO;
    private static YAMLMapper yamlMapper;


    @BeforeAll
    static void setUp() {
        tokenDTO = new TokenDTO();
        yamlMapper = new YAMLMapper();
    }

    @Test
    @Order(1)
    void signIn() throws JsonProcessingException {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("jeraldo", "admin123");
        String yamlBody = yamlMapper.writeValueAsString(credentials);

        var response = given()
                .basePath("/auth/signin")
                .port(TestsConfigs.SERVER_PORT)
                .config(config.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                .contentType("application/yaml")
                .accept("application/yaml")
                .body(yamlBody)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType("application/yaml")
                .extract()
                .asString();

        tokenDTO = yamlMapper.readValue(response, TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(2)
    void refreshToken() throws JsonProcessingException {

        var response = given()
                .basePath("/auth/refresh")
                .port(TestsConfigs.SERVER_PORT)
                .config(config.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                .contentType("application/yaml")
                .accept("application/yaml")
                .pathParam("username", tokenDTO.getUsername())
                .header(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .contentType("application/yaml")
                .extract()
                .asString();

        tokenDTO = yamlMapper.readValue(response, TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());

    }
}