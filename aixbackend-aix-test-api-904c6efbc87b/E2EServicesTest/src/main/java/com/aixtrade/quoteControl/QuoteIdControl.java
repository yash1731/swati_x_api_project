package com.aixtrade.quoteControl;

import com.aixtrade.schemaModel.BulkQuoteCancel;
import com.aixtrade.httpsVerbs.DeleteVerb;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.TestData;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuoteIdControl {

    public final List<Long> quoteIds = new CopyOnWriteArrayList<>();
    public final Set<String> traderIds = new HashSet<>();

    @Getter
    private long quoteId;

    private final DeleteVerb deleteVerb;

    public long storeQuoteId(long newQuoteId) {
        quoteIds.add(newQuoteId);
        quoteId = newQuoteId;
        return newQuoteId;
    }

    public String storeTraderId(String newTraderId) {
        traderIds.add(newTraderId);
        return newTraderId;
    }

    public void cancelQuoteIds() {
//        List<Integer> quotes = quoteIds;
        log.debug("Found quote ids" + quoteIds);
        quoteIds.forEach(id -> {
            String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
            String uri = TestData.getValue("cancelQuote uri");
            String endpoint = baseEndpoint + uri + "/" + id;
//            DeleteVerb deleteVerb = new DeleteVerb();
            Response response = deleteVerb.deleteOnApi(endpoint, null);
//            if (response.statusCode() < 400) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        });
    }

    public void resetQuoteIds() {
        quoteIds.clear();
    }

    public void bulkCancelQuotes() {
        String symbol = TestData.getValue("symbol requested");
        traderIds.forEach(traderId -> {
            String baseEndpoint = EnvConfig.getValue("quoteService baseUrl");
            String uri = TestData.getValue("bulkCancelQuote uri");
            String endpoint = baseEndpoint + uri + "/";
            BulkQuoteCancel quote = new BulkQuoteCancel();
            quote.setUserId(traderId);
            quote.setSymbol(symbol);
//            DeleteVerb deleteVerb = new DeleteVerb();
            Response response = deleteVerb.deleteOnApiUsingBody(endpoint, quote);
            if (response.statusCode() < 400) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void resetTraderIds() {
        traderIds.clear();
    }
}
