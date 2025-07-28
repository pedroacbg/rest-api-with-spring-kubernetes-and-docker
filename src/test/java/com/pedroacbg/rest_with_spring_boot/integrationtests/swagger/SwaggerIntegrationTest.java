package com.pedroacbg.rest_with_spring_boot.integrationtests.swagger;

import com.pedroacbg.rest_with_spring_boot.config.TestsConfigs;
import com.pedroacbg.rest_with_spring_boot.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@Test
	void shouldDisplaySwaggerUIPage() {
		var content = given().basePath("/swagger-ui/index.html")
						.port(TestsConfigs.SERVER_PORT)
						.when().get().then().statusCode(200)
						.extract().body().asString();
		assertTrue(content.contains("Swagger UI"));
	}

}
