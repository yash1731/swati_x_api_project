package com.aixtrade.streams.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;
import org.skyscreamer.jsonassert.comparator.JSONCompareUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.awaitility.Awaitility.given;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseKafkaEventVerifier<T extends KafkaStreamEventPayload> {

    private static final long WAIT_FOR_EVENT_IN_MILLIS = 15000L;

    private final List<T> eventPayloads = new CopyOnWriteArrayList<>();

    @Autowired
    private ObjectMapper objectMapper;

    private final AtomicBoolean eventPayloadsLoaded = new AtomicBoolean(false);

    protected void shutDownExecutor(ExecutorService executor) {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

    protected void add(T payload) {
        eventPayloads.add(payload);

        if (eventPayloads.size() > 0) {
            eventPayloadsLoaded.set(true);
        }
    }

    public void reset() {
        eventPayloads.clear();
        eventPayloadsLoaded.set(false);
    }

    public List<T> getEventPayloads() {
        return Collections.unmodifiableList(eventPayloads);
    }

    public void assertJsonObjectMatch(Object expectedResponse) {
        try {
            assertJsonStringMatch(expectedResponse);
        } catch (JsonProcessingException e) {
            log.error(format("Error while trying to parse json, %nexpected: %s %nactual: %s", expectedResponse, eventPayloads), e);
        }
    }

    public void assertPredicateMatch(Predicate<T> predicate) {

        given()
                .await()
                .pollDelay(Duration.ofMillis(0))
                .pollInterval(Duration.ofMillis(10L))
                .atMost(WAIT_FOR_EVENT_IN_MILLIS, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {

                    waitForEventPayloads();

                    assertTrue(findEventPayload(predicate).isPresent());
                });
    }

    public Optional<T> findEventPayload(Predicate<T> predicate) {
        return eventPayloads.stream().filter(Objects::nonNull).filter(predicate).findFirst();
    }

    public List<T> findEventPayloads(Predicate<T> predicate) {
        List<T> payloads = new ArrayList<>();

        given()
                .await()
                .pollDelay(Duration.ofMillis(0))
                .pollInterval(Duration.ofMillis(10L))
                .atMost(WAIT_FOR_EVENT_IN_MILLIS, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {

                    waitForEventPayloads();

                    final List<T> foundEventPayloads =
                            eventPayloads.stream().filter(predicate).collect(Collectors.toList());

                    if (!foundEventPayloads.isEmpty()) {
                        payloads.addAll(foundEventPayloads);
                    }

                    assertFalse(foundEventPayloads.isEmpty());
                });
        return payloads;
    }

    private void assertJsonStringMatch(Object expectedResponse) throws JsonProcessingException {

        final String expectedResponseAsString = convertToString(expectedResponse);

        given()
                .await()
                .pollDelay(Duration.ofMillis(0))
                .pollInterval(Duration.ofMillis(10L))
                .atMost(WAIT_FOR_EVENT_IN_MILLIS, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {

                    waitForEventPayloads();

                    String actualResponseAsString = convertToString(eventPayloads);

                    JSONAssert
                            .assertEquals(
                                    format("%nexpected: %s %nactual: %s", expectedResponseAsString, actualResponseAsString),
                                    expectedResponseAsString,
                                    actualResponseAsString,
                                    new KafkaEventJsonComparator(JSONCompareMode.LENIENT));
                });
    }

    private void waitForEventPayloads() {
        given()
                .await()
                .pollDelay(Duration.ofMillis(0))
                .pollInterval(Duration.ofMillis(10L))
                .atMost(WAIT_FOR_EVENT_IN_MILLIS, TimeUnit.MILLISECONDS)
                .untilTrue(eventPayloadsLoaded);
    }

    private String convertToString(Object value) throws JsonProcessingException {

        if (value instanceof List) {

            final List<Map<String, Object>> listOfMaps =
                    convertObject(value, new TypeReference<List<Map<String, Object>>>() {
                    });

            final List<Map<String, Object>> modifiedListOfMaps =
                    listOfMaps.stream().peek(removeNullValues()).collect(Collectors.toList());

            return objectMapper.writeValueAsString(modifiedListOfMaps);
        } else {
            final Map<String, Object> maps =
                    convertObject(value, new TypeReference<Map<String, Object>>() {
                    });

            removeNullValues().accept(maps);

            return objectMapper.writeValueAsString(maps);
        }
    }

    private <T> T convertObject(Object value, TypeReference<T> typeReference) {
        return objectMapper.convertValue(value, typeReference);
    }

    private Consumer<Map<String, Object>> removeNullValues() {
        return m -> m.values().removeIf(Objects::isNull);
    }

    static class KafkaEventJsonComparator extends DefaultComparator {

        private final JSONCompareMode mode;

        public KafkaEventJsonComparator(JSONCompareMode mode) {
            super(mode);
            this.mode = mode;
        }

        public void compareJSONArray(String prefix, JSONArray expected, JSONArray actual, JSONCompareResult result) throws JSONException {
            if (expected.length() != 0) {
                if (this.mode.hasStrictOrder()) {
                    this.compareJSONArrayWithStrictOrder(prefix, expected, actual, result);
                } else if (JSONCompareUtil.allSimpleValues(expected)) {
                    this.compareJSONArrayOfSimpleValues(prefix, expected, actual, result);
                } else if (JSONCompareUtil.allJSONObjects(expected)) {
                    this.compareJSONArrayOfJsonObjects(prefix, expected, actual, result);
                } else {
                    this.recursivelyCompareJSONArray(prefix, expected, actual, result);
                }

            }
        }
    }
}
