package com.aixtrade.streams;

import com.aixtrade.streams.order.OrderStreamResponse;
import com.aixtrade.streams.quoterequest.QuoteRequestStreamResponse;
import com.aixtrade.streams.quoteresponse.QuoteResponseStreamResponse;
import com.aixtrade.streams.quote.QuoteStreamResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.lang.String.format;

@Slf4j
@Component
public class StreamWorldHelper {

    private static final long WAIT_FOR_STREAM_UPDATE_IN_SECONDS = 10L;
    private static final long WAIT_FOR_OPEN_ORDER_STREAM_UPDATE_IN_SECONDS = 10L;
    private static CountDownLatch orderStreamCountDownLatch;
    private static CountDownLatch quoteRequestStreamCountDownLatch;
    private static CountDownLatch quoteStreamCountDownLatch;
    private static CountDownLatch quoteResponseStreamCountDownLatch;
    private static CountDownLatch streamCountDownLatch;
//    private static Integer streamCountDownLatchOriginalCount;
    private static final Multimap<String, OrderStreamResponse> orderStreamUpdates = Multimaps.synchronizedMultimap(HashMultimap.create());
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Gson gson = new Gson();
    private static final Multimap<String, QuoteRequestStreamResponse> quoteRequestStreamUpdates = Multimaps.synchronizedMultimap(HashMultimap.create());
    private static final Multimap<String, QuoteStreamResponse> quoteStreamUpdates = Multimaps.synchronizedMultimap(HashMultimap.create());
    private static final Multimap<String, QuoteResponseStreamResponse> quoteResponseStreamUpdates = Multimaps.synchronizedMultimap(HashMultimap.create());


    public void initCountDownLatch(
            List<StreamUpdateCount> streamUpdateCounts,
            String name,
            Consumer<StreamUpdateCount> streamUpdateCountConsumer) {

        streamUpdateCounts
                .stream()
                .filter(streamUpdateCount ->
                        name.equals(streamUpdateCount.getName()))
                .findFirst()
                .ifPresent(streamUpdateCountConsumer);
    }

    public void initOrderStreamCountDownLatch(Integer count) {
        orderStreamUpdates.clear();
        orderStreamCountDownLatch = new CountDownLatch(count);
    }

    public void initQuoteRequestStreamCountDownLatch(Integer count) {
        quoteRequestStreamUpdates.clear();
        quoteRequestStreamCountDownLatch = new CountDownLatch(count);
    }

    public void initQuoteStreamCountDownLatch(Integer count) {
        quoteStreamUpdates.clear();
        quoteStreamCountDownLatch = new CountDownLatch(count);
    }

    public void initQuoteResponseStreamCountDownLatch(Integer count) {
        quoteResponseStreamUpdates.clear();
        quoteResponseStreamCountDownLatch = new CountDownLatch(count);
    }

    public void awaitStreamCountDownLatch() throws InterruptedException {
        streamCountDownLatch.await(WAIT_FOR_STREAM_UPDATE_IN_SECONDS, TimeUnit.SECONDS);
    }

    public void awaitOrderStreamCountDownLatch() throws InterruptedException {
        orderStreamCountDownLatch.await(WAIT_FOR_STREAM_UPDATE_IN_SECONDS, TimeUnit.SECONDS);
    }

    public void awaitQuoteRequestStreamCountDownLatch() throws InterruptedException {
        quoteRequestStreamCountDownLatch.await(WAIT_FOR_STREAM_UPDATE_IN_SECONDS, TimeUnit.SECONDS);
    }

    public void awaitQuoteStreamCountDownLatch() throws InterruptedException {
        quoteStreamCountDownLatch.await(WAIT_FOR_STREAM_UPDATE_IN_SECONDS, TimeUnit.SECONDS);
    }

    public void awaitQuoteResponseStreamCountDownLatch() throws InterruptedException {
        quoteResponseStreamCountDownLatch.await(WAIT_FOR_STREAM_UPDATE_IN_SECONDS, TimeUnit.SECONDS);
    }

    //    ORDERS
    public List<OrderStreamResponse> getOrderStreamUpdates() {
        return new ArrayList<>(orderStreamUpdates.values());
    }

    public static void resetOrderUpdates() {
        orderStreamUpdates.clear();
    }

    public static void addOrderUpdates(Map<String, Object> data) throws JsonProcessingException {

        final OrderStreamResponse streamUpdate = convertToObject(objectMapper.writeValueAsString(data), OrderStreamResponse.class);

//        System.out.println(format("adding order update: %s", streamUpdate));

        final String symbol = streamUpdate.getSymbol();
        final Collection<OrderStreamResponse> orderStreamResponses = orderStreamUpdates.get(symbol);

        synchronized (orderStreamUpdates) {

            if (orderStreamResponses.isEmpty() || !orderInList(orderStreamResponses, streamUpdate)) {
                orderStreamUpdates.put(symbol, streamUpdate);
                if (orderStreamCountDownLatch != null) {
                    log.info("OrderStreamResponse ({}): {}", orderStreamCountDownLatch.getCount(), streamUpdate);
                    orderStreamCountDownLatch.countDown();
                }
            }
        }
    }

    public static String convertToString(Object object) {
        return gson.toJson(object);
    }

    private static <T> T convertToObject(String body, Class<T> clazz) {
        return gson.fromJson(body, clazz);
    }

    private static boolean orderInList(Collection<OrderStreamResponse> actualStreamResponses, OrderStreamResponse expectedStreamResponse) {
        return
                actualStreamResponses
                        .stream()
                        .anyMatch(actualStreamResponse -> ordersMatch(actualStreamResponse, expectedStreamResponse));
    }


    private static boolean ordersMatch(OrderStreamResponse actualStreamResponse, OrderStreamResponse expectedStreamResponse) {
        return actualStreamResponse.getSymbol().equals(expectedStreamResponse.getSymbol()) &&
                actualStreamResponse.getSide().equals(expectedStreamResponse.getSide()) &&
                actualStreamResponse.getQuantity().equals(expectedStreamResponse.getQuantity()) &&
                actualStreamResponse.getOriginalQuantity().equals(expectedStreamResponse.getOriginalQuantity()) &&
                actualStreamResponse.getLiveQuantity().equals(expectedStreamResponse.getLiveQuantity()) &&
                actualStreamResponse.getExecutionQuantity().equals(expectedStreamResponse.getExecutionQuantity()) &&
                actualStreamResponse.getTrader().equals(expectedStreamResponse.getTrader()) &&
                actualStreamResponse.getOrderType().equals(expectedStreamResponse.getOrderType()) &&
                actualStreamResponse.getOrderStatus().equals(expectedStreamResponse.getOrderStatus());
    }


    private void assertJsonStringMatch(String expectedResponse, String actualResponse) throws JSONException {
        JSONAssert
                .assertEquals(
                        format("%nexpected: %s %nactual: %s", expectedResponse, actualResponse),
                        expectedResponse,
                        actualResponse,
                        JSONCompareMode.LENIENT);
    }

    public void assertJsonObjectMatch(Object expectedResponse, Object actualResponse) throws JSONException {
        final String expectedAsString = convertToString(expectedResponse);
        final String actualAsString = convertToString(actualResponse);
        assertJsonStringMatch(expectedAsString, actualAsString);
    }


    //    QUOTE REQUESTS
    public List<QuoteRequestStreamResponse> getQuoteRequestStreamUpdates() {
        return new ArrayList<>(quoteRequestStreamUpdates.values());
    }

    public static void resetQuoteRequestUpdates() {
        quoteRequestStreamUpdates.clear();
    }

    private static boolean quoteRequestInList(Collection<QuoteRequestStreamResponse> actualQuoteRequestStreamResponses, QuoteRequestStreamResponse expectedStreamResponse) {
        return
                actualQuoteRequestStreamResponses
                        .stream()
                        .anyMatch(actualQuoteRequestStreamResponse -> quoteRequestMatch(actualQuoteRequestStreamResponse, expectedStreamResponse));
    }

    public static void addQuoteRequestUpdates(Map<String, Object> data) throws JsonProcessingException {

        final QuoteRequestStreamResponse quoteRequestStreamUpdate = convertToObject(objectMapper.writeValueAsString(data), QuoteRequestStreamResponse.class);

//        System.out.println(format("adding quote request update: %s", quoteRequestStreamUpdate));

        final String symbol = quoteRequestStreamUpdate.getSymbol();
        final Long quoteRequestId = quoteRequestStreamUpdate.getQuoteRequestId();
        final Collection<QuoteRequestStreamResponse> quoteRequestStreamResponses = quoteRequestStreamUpdates.get(symbol);

        synchronized (quoteRequestStreamUpdates) {

            if (quoteRequestStreamResponses.isEmpty() /*|| !quoteRequestInList(quoteRequestStreamResponses, quoteRequestStreamUpdate) */) {
                quoteRequestStreamUpdates.put(String.valueOf(quoteRequestId), quoteRequestStreamUpdate);
                if (quoteRequestStreamCountDownLatch != null) {
                    log.info("QuoteRequestStreamResponse ({}): {}", quoteRequestStreamCountDownLatch.getCount(), quoteRequestStreamUpdate);
                    quoteRequestStreamCountDownLatch.countDown();
                }
            }
        }
    }


    private static boolean quoteRequestMatch(QuoteRequestStreamResponse actualQuoteRequestStreamResponse, QuoteRequestStreamResponse expectedStreamResponse) {
        return actualQuoteRequestStreamResponse.getSymbol().equals(expectedStreamResponse.getSymbol()) &&
                actualQuoteRequestStreamResponse.getChannel().equals(expectedStreamResponse.getChannel()) &&
                actualQuoteRequestStreamResponse.getQuantity().equals(expectedStreamResponse.getQuantity()) &&
                actualQuoteRequestStreamResponse.getDirection().equals(expectedStreamResponse.getDirection()) &&
                actualQuoteRequestStreamResponse.getStatus().equals(expectedStreamResponse.getStatus()) &&
                actualQuoteRequestStreamResponse.getTrader().equals(expectedStreamResponse.getTrader()) //&&
//                actualQuoteRequestStreamResponse.getLatestQuote().getBid().getPrice().equals(expectedStreamResponse.getLatestQuote().getBid().getPrice()) &&
//                actualQuoteRequestStreamResponse.getLatestQuote().getOffer().getPrice().equals(expectedStreamResponse.getLatestQuote().getOffer().getPrice()) &&
//                actualQuoteRequestStreamResponse.getLatestQuote().getBid().getPrice().equals(expectedStreamResponse.getLatestQuote().getBid().getPrice())
                ;
    }

    //    QUOTES
    public List<QuoteStreamResponse> getQuoteStreamUpdates() {
        return new ArrayList<>(quoteStreamUpdates.values());
    }

    public static void resetQuoteUpdates() {
        quoteStreamUpdates.clear();
    }

    private static boolean quoteInList(Collection<QuoteStreamResponse> actualQuoteStreamResponses, QuoteStreamResponse expectedStreamResponse) {
        return
                actualQuoteStreamResponses
                        .stream()
                        .anyMatch(actualQuoteStreamResponse -> quoteMatch(actualQuoteStreamResponse, expectedStreamResponse));
    }

    public static void addQuoteUpdates(Map<String, Object> data) throws JsonProcessingException {

        final QuoteStreamResponse quoteStreamUpdate = convertToObject(objectMapper.writeValueAsString(data), QuoteStreamResponse.class);
//        System.out.println(format("adding quote update: %s", quoteStreamUpdate));
        final String symbol = quoteStreamUpdate.getSymbol();
        final Integer quoteId = quoteStreamUpdate.getQuoteId();
        final Collection<QuoteStreamResponse> quoteStreamResponses = quoteStreamUpdates.get(symbol);

        synchronized (quoteStreamUpdates) {

            if (quoteStreamResponses.isEmpty() /*|| !quoteInList(quoteStreamResponses, quoteStreamUpdate) */) {
                quoteStreamUpdates.put(String.valueOf(quoteId), quoteStreamUpdate);
                if (quoteStreamCountDownLatch != null) {
                    log.info("QuoteStreamResponse ({}): {}", quoteStreamCountDownLatch.getCount(), quoteStreamUpdate);
                    quoteStreamCountDownLatch.countDown();
                }
            }
        }
    }

    private static boolean quoteMatch(QuoteStreamResponse actualQuoteStreamResponse, QuoteStreamResponse expectedStreamResponse) {
        return actualQuoteStreamResponse.getSymbol().equals(expectedStreamResponse.getSymbol()) &&
                actualQuoteStreamResponse.getChannel().equals(expectedStreamResponse.getChannel()) &&
                actualQuoteStreamResponse.getRequestedQuantity().equals(expectedStreamResponse.getRequestedQuantity()) &&
                actualQuoteStreamResponse.getDirection().equals(expectedStreamResponse.getDirection()) &&
                actualQuoteStreamResponse.getStatus().equals(expectedStreamResponse.getStatus()) &&
                actualQuoteStreamResponse.getTrader().equals(expectedStreamResponse.getTrader()) &&
                actualQuoteStreamResponse.getBid().getPrice().equals(expectedStreamResponse.getBid().getPrice()) &&
//                Objects.nonNull(actualQuoteStreamResponse.getBid().getExecutedPrice()) && actualQuoteStreamResponse.getBid().getExecutedPrice().equals(expectedStreamResponse.getBid().getExecutedPrice()) &&
//                Objects.nonNull(actualQuoteStreamResponse.getBid().getExecutedQuantity()) && actualQuoteStreamResponse.getBid().getExecutedQuantity().equals(expectedStreamResponse.getBid().getExecutedQuantity()) &&
                actualQuoteStreamResponse.getOffer().getPrice().equals(expectedStreamResponse.getOffer().getPrice())
//                Objects.nonNull(actualQuoteStreamResponse.getOffer().getExecutedPrice()) && actualQuoteStreamResponse.getOffer().getExecutedPrice().equals(expectedStreamResponse.getOffer().getExecutedPrice()) &&
//                Objects.nonNull(actualQuoteStreamResponse.getOffer().getExecutedQuantity()) && actualQuoteStreamResponse.getOffer().getExecutedQuantity().equals(expectedStreamResponse.getOffer().getExecutedQuantity())
                ;
    }

    //    QUOTE RESPONSE
    public List<QuoteResponseStreamResponse> getQuoteResponseStreamUpdates() {
        return new ArrayList<>(quoteResponseStreamUpdates.values());
    }

    public static void resetQuoteResponseUpdates() {
        quoteResponseStreamUpdates.clear();
    }

    private static boolean quoteResponseInList(Collection<QuoteResponseStreamResponse> actualQuoteResponseStreamResponses, QuoteResponseStreamResponse expectedStreamResponse) {
        return
                actualQuoteResponseStreamResponses
                        .stream()
                        .anyMatch(actualQuoteResponseStreamResponse -> quoteResponseMatch(actualQuoteResponseStreamResponse, expectedStreamResponse));
    }

    public static void addQuoteResponseUpdates(Map<String, Object> data) throws JsonProcessingException {

        final QuoteResponseStreamResponse quoteResponseStreamUpdate = convertToObject(objectMapper.writeValueAsString(data), QuoteResponseStreamResponse.class);

//        System.out.println(format("adding quote response update: %s", quoteResponseStreamUpdate));

        final String symbol = quoteResponseStreamUpdate.getSymbol();
        final Collection<QuoteResponseStreamResponse> quoteResponseStreamResponses = quoteResponseStreamUpdates.get(symbol);

        synchronized (quoteResponseStreamUpdates) {

            if (quoteResponseStreamResponses.isEmpty() || !quoteResponseInList(quoteResponseStreamResponses, quoteResponseStreamUpdate)) {
                quoteResponseStreamUpdates.put(symbol, quoteResponseStreamUpdate);
                if (quoteResponseStreamCountDownLatch != null) {
                    log.info("QuoteResponseStreamResponse ({}): {}", quoteResponseStreamCountDownLatch.getCount(), quoteResponseStreamUpdate);
                    quoteResponseStreamCountDownLatch.countDown();
                }
            }
        }
    }


    private static boolean quoteResponseMatch(QuoteResponseStreamResponse actualQuoteResponseStreamResponse, QuoteResponseStreamResponse expectedStreamResponse) {
        return actualQuoteResponseStreamResponse.getSymbol().equals(expectedStreamResponse.getSymbol()) &&
                actualQuoteResponseStreamResponse.getChannel().equals(expectedStreamResponse.getChannel()) &&
                actualQuoteResponseStreamResponse.getQuantity().equals(expectedStreamResponse.getQuantity()) &&
                actualQuoteResponseStreamResponse.getDirection().equals(expectedStreamResponse.getDirection()) &&
                actualQuoteResponseStreamResponse.getStatus().equals(expectedStreamResponse.getStatus()) &&
                actualQuoteResponseStreamResponse.getTrader().equals(expectedStreamResponse.getTrader());
    }
}
