package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.support.WorldHelper;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.JsonReader;
import com.aixtrade.utilities.TestData;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class ExpiringQuoteRequestEventSteps extends End2EndTest {

    private final WorldHelper helper;
    private Response response;

//    public ExpiringQuoteRequestEventSteps(WorldHelper helper) {
//        this.helper = helper;
//    }

    @When("^I submit a Quote Request$")
    public void iSubmitAQuoteRequest() throws IOException, InterruptedException {
//        Thread.sleep(1000);
        String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
        String uri = TestData.getValue("createQuoteRequest uri");
        String endpoint = baseEndpoint + uri;
        String data = JsonReader.getData("3123CreateQuoteRequest.json");
        response = helper.postVerb().postOnApi(endpoint, data);
    }

    @When("^my Quote Request is about to expire$")
    public void myQuoteRequestIsAboutToExpire() {
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        int quoteRequestId = jsonPath.get("quoteRequest.quoteRequestId");
        System.out.println("Quote request id is " + quoteRequestId);
        helper.getQuoteRequestIdControl().storeQuoteRequestId(quoteRequestId);
    }
}
