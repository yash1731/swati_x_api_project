package com.aixtrade.hooks;


import com.aixtrade.orderControl.OrderIdControl;
import com.aixtrade.quoteControl.QuoteIdControl;
import com.aixtrade.quoteControl.QuoteRequestIdControl;
import com.aixtrade.streams.kafka.OrderKafkaEventVerifier;
import com.aixtrade.streams.kafka.QuoteKafkaEventVerifier;
import com.aixtrade.streams.kafka.QuoteRequestKafkaEventVerifier;
import com.aixtrade.streams.kafka.QuoteResponseKafkaEventVerifier;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScenarioHooks {

    private final OrderIdControl orderIdControl;
    private final QuoteIdControl quoteIdControl;
    private final QuoteRequestIdControl quoteRequestIdControl;

    private final OrderKafkaEventVerifier orderKafkaEventVerifier;
    private final QuoteRequestKafkaEventVerifier quoteRequestKafkaEventVerifier;
    private final QuoteResponseKafkaEventVerifier quoteResponseKafkaEventVerifier;
    private final QuoteKafkaEventVerifier quoteKafkaEventVerifier;

    @Before(order = 2)
    public void beforeTests() {
//        OrderStreamFactory.subscribeToOrderbookStreams();
//        OrderKafkaStreamFactory.subscribeToOrderbookStreams();
//        QuoteRequestStreamFactory.subscribeToQuoteRequestStreams();
//        QuoteStreamFactory.subscribeToQuoteStreams();
//        QuoteResponseStreamFactory.subscribeToQuoteResponseStreams();
        orderIdControl.resetOrderIds();
        quoteRequestIdControl.resetQuoteRequestIds();
        quoteIdControl.resetQuoteIds();
        quoteIdControl.resetTraderIds();
//        StreamWorldHelper.resetOrderUpdates();
//        StreamWorldHelper.resetQuoteRequestUpdates();
//        StreamWorldHelper.resetQuoteUpdates();
//        StreamWorldHelper.resetQuoteResponseUpdates();
    }

//    @After(value = "@cancelOrder", order = 0)
//    public void afterCancelOrders() {
//        orderIdControl.deleteOrderIds();
//    }
//
//    @After(value = "@cancelTwoWayOrder", order = 1)
//    public void afterCancelTwoWayOrders() {
//        orderIdControl.deleteTwoWayOrderIds();
//    }
//
//    @After(value = "@cancelQuote", order = 2)
//    public void afterCancelQuote() {
//        quoteIdControl.cancelQuoteIds();
//    }
//
//    @After(value = "@bulkCancel", order = 3)
//    public void afterBulkCancel() {
//        quoteIdControl.bulkCancelQuotes();
//    }
//
//    @After(value = "@cancelQR", order = 4)
//    public void afterCancelQR() throws InterruptedException {
//        quoteRequestIdControl.cancelQuoteRequestIds();
//    }

    @After(value = "@cancel", order = 0)
//    @After(order = 0)
    public void afterCancelQR() throws InterruptedException {
        orderIdControl.deleteOrderIds();
        orderIdControl.deleteTwoWayOrderIds();
        quoteIdControl.cancelQuoteIds();
        quoteIdControl.bulkCancelQuotes();
        quoteRequestIdControl.cancelQuoteRequestIds();
    }

    @After(order = 100)
    public void afterTests() {
        orderKafkaEventVerifier.reset();
        quoteRequestKafkaEventVerifier.reset();
        quoteResponseKafkaEventVerifier.reset();
        quoteKafkaEventVerifier.reset();
    }
}
