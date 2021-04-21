package com.aixtrade.httpsVerbs;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;

@Slf4j
public abstract class BaseVerb {

    protected RequestSpecification requestSpecification;
    @Getter
    protected Response response;

    BaseVerb() {
        requestSpecification = given().contentType("application/json").accept("application/json");
    }

//    public RequestSpecification getRequestSpecification() {
//        return requestSpecification;
//    }

//    public RequestSpecification setUp() {
//        requestSpecification = given().contentType("application/json").accept("application/json");
//        return requestSpecification;
//    }

    void printResponse(String message) {
        if (isError(response)) {
            log.warn(message);
        } else {
            log.info(message);
        }
    }

    protected boolean isError(Response response) {
        return response.statusCode() > 399;
    }
}
