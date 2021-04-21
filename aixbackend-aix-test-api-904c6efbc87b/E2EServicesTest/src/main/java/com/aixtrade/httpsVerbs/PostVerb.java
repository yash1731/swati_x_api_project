package com.aixtrade.httpsVerbs;

import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class PostVerb extends BaseVerb {

//    public Response getResponse() {
//        return response;
//    }

    public Response postOnApi(String uri, Object body) {
        response = requestSpecification.body(body).post(uri);

        printResponse(format("\nPOST %s, \nbody: %s, \nresponse: %s\n", uri, body, response.asString()));

        return response;
    }
}
