package com.aixtrade.httpsVerbs;

import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class GetVerb extends BaseVerb {

//    public Response getResponse() {
//        return response;
//    }

    public Response getApi(String uri) {
        response = requestSpecification.get(uri);

        printResponse(format("%nGET %s, %nresponse: %s%n", uri, response.asString()));

        return response;
    }
}
