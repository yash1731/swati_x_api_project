package com.aixtrade.quoteControl;

import com.aixtrade.httpsVerbs.DeleteVerb;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.TestData;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuoteRequestIdControl {

    public final List<Integer> quoteRequestIds = new CopyOnWriteArrayList<>();

    @Getter
    private int quoteRequestId;

    private final DeleteVerb deleteVerb;

    public int storeQuoteRequestId(int newQuoteRequestId) {
        quoteRequestIds.add(newQuoteRequestId);
        quoteRequestId = newQuoteRequestId;
        return newQuoteRequestId;
    }

    public void cancelQuoteRequestIds() throws InterruptedException {
//        List<Integer> quoteRequests = quoteRequestIds;
        log.debug("Found quote request ids" + quoteRequestIds);
        quoteRequestIds.forEach(id -> {
            String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
            String uri = TestData.getValue("cancelQuoteRequest uri");
            String endpoint = baseEndpoint + uri + "/" + id;
//            DeleteVerb deleteVerb = new DeleteVerb();
            Response response = deleteVerb.deleteOnApi(endpoint, null);
            if (response.statusCode() < 400) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void resetQuoteRequestIds() {
        quoteRequestIds.clear();
    }
}
