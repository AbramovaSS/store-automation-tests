package org.example.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.config.ConfigReader;

public class RestApiBuilder {
    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.CONFIG.baseUrl())
                .setBasePath(ConfigReader.CONFIG.basePath())
                .setContentType(ContentType.JSON)
                .setAuth(RestAssured.basic(
                        ConfigReader.CONFIG.userLogin(),
                        ConfigReader.CONFIG.userPassword()
                ))
                .log(LogDetail.ALL)
                .build();
    }
}
