package com.aixtrade.httpsVerbs;

import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class DeleteVerb extends com.aixtrade.httpsVerbs.BaseVerb {

//    public Response getResponse() {
//        return response;
//    }

    public Response deleteOnApi(String uri, Object body) {
        response = requestSpecification.delete(uri);

//        printResponse(format("%nDELETE %s, %nbody: %s, %nresponse: %s%n", uri, body, response.asString()));

        return response;
    }

    public Response deleteOnApiUsingBody(String uri, Object body) {
        response = requestSpecification.body(body).delete(uri);

//        printResponse(format("%nDELETE %s, %nbody: %s, %nresponse: %s%n", uri, body, response.asString()));

        return response;
    }
}
