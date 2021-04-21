package com.aixtrade.streams.quote;

import com.aixtrade.streams.BaseStreamFactory;
//import com.aixtrade.streams.StreamWorldHelper;
//import com.aixtrade.utilities.EnvConfig;
//import com.aixtrade.utilities.StreamsReader;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Signal;
//import reactor.core.scheduler.Schedulers;
//
//import java.util.Map;
//import java.util.Objects;
//import java.util.concurrent.Executors;
//
//import static java.lang.String.format;
//import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

public class QuoteStreamFactory extends BaseStreamFactory {

//    private static String quoteBaseUrl = EnvConfig.getValue("quoteService baseUrl");
//    private static String quoteUri = StreamsReader.getValue("quoteStream.uri");
//    private static WebClient webClient;
//
//
//    public static void subscribeToQuoteStreams() {
//        if (webClient == null) {
//            webClient =
//                    WebClient
//                            .builder()
//                            .baseUrl(quoteBaseUrl)
//                            .defaultHeader("Content-Type", APPLICATION_STREAM_JSON_VALUE)
//                            .build();
//        }
//        startSubscriptionToQuoteStreams("quote");
//    }
//
//    private static void startSubscriptionToQuoteStreams(String streamName) {
//        System.out.println(format("establishing %s stream \n", streamName));
//        Executors.newCachedThreadPool().execute(() -> {
//            webClient.get()
//                    .uri(quoteUri)
//                    .accept(MediaType.APPLICATION_STREAM_JSON)
//                    .exchange()
//                    .flatMapMany(response -> response.bodyToFlux(Map.class))
////                    .doOnEach(new Consumer<Signal<Map>>() {
////                        @Override
////                        public void accept(Signal<Map> mapSignal) {
////                            System.out.println("response: " + mapSignal.get());
////                        }
////                    })
//                    .publishOn(Schedulers.parallel())
//                    .subscribe(
//                            body -> {
////                                System.out.println("body quote: " + body);
//                                if (body.containsKey("createdQuoteEvent")) {
//                                    final Map<String, Object> event = (Map<String, Object>) body.get("createdQuoteEvent");
////                                    Object symbol = event.get("symbol");
////                                    if (symbol != null && "LTCUSD".equalsIgnoreCase(symbol.toString())) {
//                                    if (event != null) {
////                                        System.out.println(format("Quote stream received: %s", event));
//                                        try {
//                                            event.put("name", "createdQuoteEvent");
//                                            StreamWorldHelper.addQuoteUpdates(event);
//                                        } catch (JsonProcessingException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//                                if (body.containsKey("improvedQuoteEvent")) {
//                                    final Map<String, Object> event = (Map<String, Object>) body.get("improvedQuoteEvent");
//                                    if (event != null) {
//
//                                        final Map<String, Object> improvedQuote = (Map<String, Object>) event.get("improvedQuote");
//
//                                        if (Objects.nonNull(improvedQuote)) {
//
////                                            System.out.println(format("Quote stream received: %s", event));
//                                            try {
//                                                improvedQuote.put("name", "improvedQuoteEvent");
//                                                StreamWorldHelper.addQuoteUpdates(improvedQuote);
//                                            } catch (JsonProcessingException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }
//                                }
//                            },
//                            err -> {
//                                System.err.println(String.format("Failed to retrieve payload from quote stream, error: %s", err));
//                            },
//                            () -> System.out.println("Quote stream stopped"));
//        });
//    }
}
