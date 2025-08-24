package com.pedroacbg.rest_with_spring_boot.config;

public interface TestsConfigs {

    int SERVER_PORT = 80;

    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";

    String ORIGIN_LOCALHOST = "http://localhost:8080/";
    String ORIGIN_GULERONE = "https://gulerone-animes.netlify.app/";
    String ORIGIN_GOOGLE = "https://google.com";
}
