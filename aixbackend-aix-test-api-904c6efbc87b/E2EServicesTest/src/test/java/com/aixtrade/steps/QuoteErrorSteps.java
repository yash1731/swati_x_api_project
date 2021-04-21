package com.aixtrade.steps;

import com.aixtrade.End2EndTest;
import com.aixtrade.streams.StreamWorldHelper;
import com.aixtrade.support.WorldHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RequiredArgsConstructor
public class QuoteErrorSteps extends End2EndTest {

    private final WorldHelper helper;
    private Response response;
    private String responseBody;


//    public QuoteErrorSteps(WorldHelper helper, StreamWorldHelper streamWorldHelper) {
//        this.helper = helper;
//    }

    @Then("^I should be able to see a \"([^\"]*)\" error thrown with \"([^\"]*)\" error code$")
    public void iShouldBeAbleToSeeAErrorThrownWithErrorCode(String error, int errorCode) throws Throwable {
        response = helper.postVerb().getResponse();
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        assertEquals(errorCode, statusCode);
        responseBody = response.getBody().asString();
        System.out.println("Response Body is " + responseBody);
        assertThat(responseBody.contains(error), is(true));
    }

    @Then("I should be able to see the correct status code returned")
    public void iShouldBeAbleToSeeTheCorrectStatusCodeReturned() {
        response = helper.postVerb().getResponse();
        int statusCode = response.getStatusCode();
        assertEquals(202, statusCode);
    }

    @And("I should be able to see a status of {string} returned")
    public void iShouldBeAbleToSeeAStatusOfReturned(String status) {
        response = helper.postVerb().getResponse();
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        String quoteStatus = jsonPath.get("quote.status");
        assertEquals(status, quoteStatus);
    }

    @And("I should be able to see a status of {string} returned for the Quote Response")
    public void iShouldBeAbleToSeeAStatusOfReturnedForTheQuoteResponse(String status) {
        response = helper.postVerb().getResponse();
        JsonPath jsonPath = new JsonPath(response.thenReturn().asString());
        String quoteStatus = jsonPath.get("quoteResponse.status");
        assertEquals(status, quoteStatus);
    }

    @And("^I should be able to get an overlapping error with \"([^\"]*)\" error thrown$")
    public void iShouldBeAbleToGetAnOverlappingErrorWithErrorThrown(String errorMessage) throws Throwable {
        responseBody = response.getBody().asString();
        assertThat(responseBody.contains(errorMessage), is(true));
    }

    @And("^I should be able to get an overlapping error for the bid as displayed \"([^\"]*)\"$")
    public void iShouldBeAbleToGetAnOverlappingErrorForTheBidAsDisplayed(String bidErrorMessage) {
        assertThat(responseBody.contains(bidErrorMessage), is(true));
    }

    @And("^I should be able to get an overlapping error for the Offer as displayed \"([^\"]*)\"$")
    public void iShouldBeAbleToGetAnOverlappingErrorForTheOfferAsDisplayed(String offerErrorMessage) {
        assertThat(responseBody.contains(offerErrorMessage), is(true));
    }

    @And("^I should be able to get a self matching error with \"([^\"]*)\" error thrown$")
    public void iShouldBeAbleToGetASelfMatchingErrorWithErrorThrown(String errorMessage) throws Throwable {
        responseBody = response.getBody().asString();
        assertThat(responseBody.contains(errorMessage), is(true));
    }

    @And("^I should be able to get a self matching error for the Offer as displayed \"([^\"]*)\"$")
    public void iShouldBeAbleToGetASelfMatchingErrorForTheOfferAsDisplayed(String offerErrorMessage) throws Throwable {
        assertThat(responseBody.contains(offerErrorMessage), is(true));
    }

    @And("^I should be able to get a self matching error for the Bid as displayed \"([^\"]*)\"$")
    public void iShouldBeAbleToGetASelfMatchingErrorForTheBidAsDisplayed(String bidErrorMessage) throws Throwable {
        assertThat(responseBody.contains(bidErrorMessage), is(true));
    }
}




