package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.support.WorldHelper;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.TestData;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import static org.junit.Assert.assertEquals;

@RequiredArgsConstructor
public class CancelOrdersSteps extends End2EndTest {

    private final WorldHelper helper;
    private Response response;

//    public CancelOrdersSteps(WorldHelper helper) {
//        this.helper = helper;
//    }

    @When("^I cancel order$")
    public void iCancelOrder() throws InterruptedException {
        String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
        String orderUri = TestData.getValue("orderService uri");
        String endpoint = orderBaseUrl+orderUri;
        String orderId = helper.getOrderIdControl().getOrderId();
        String endpoint2 = endpoint + "/" + orderId;
        Thread.sleep(2000);
        response = helper.deleteVerb().deleteOnApi(endpoint2, null);
    }

    @Then("^I should be able to see order cancelled$")
    public void iShouldBeAbleToSeeOrderCancelled() {
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }
}
