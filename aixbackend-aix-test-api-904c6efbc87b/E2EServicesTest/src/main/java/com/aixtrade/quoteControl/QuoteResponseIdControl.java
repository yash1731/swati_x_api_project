package com.aixtrade.quoteControl;

import com.aixtrade.httpsVerbs.DeleteVerb;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.TestData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuoteResponseIdControl {

    public final List<Integer> quoteResponseIds = new CopyOnWriteArrayList<>();

    @Getter
    private int quoteResponseId;

    private final DeleteVerb deleteVerb;

    public int storeQuoteResponseId(int newQuoteResponseId) {
        quoteResponseIds.add(newQuoteResponseId);
        quoteResponseId = newQuoteResponseId;
        return newQuoteResponseId;
    }

    public void cancelQuoteResponseIds() {
//        List<Integer> quoteResponses = quoteResponseIds;
        log.debug("Found quote response ids" + quoteResponseIds);
        quoteResponseIds.forEach(id -> {
            String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
            String uri = TestData.getValue("cancelInboundQuoteResponse uri");
            String endpoint = baseEndpoint + uri + "/" + id;
//            DeleteVerb deleteVerb = new DeleteVerb();
            deleteVerb.deleteOnApi(endpoint, null);
        });
    }

    public void resetQuoteResponseIds() {
        quoteResponseIds.clear();
    }
}
