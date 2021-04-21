package com.aixtrade.streams.quoterequest;

import com.aixtrade.streams.BaseStreamFactory;
//import com.aixtrade.streams.StreamWorldHelper;
//import com.aixtrade.utilities.EnvConfig;
//import com.aixtrade.utilities.StreamsReader;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.scheduler.Schedulers;
//
//import java.util.Map;
//import java.util.concurrent.Executors;
//
//import static java.lang.String.format;
//import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;


public class QuoteRequestStreamFactory extends BaseStreamFactory {

//    private static String quoteRequestBaseUrl = EnvConfig.getValue("quoteService baseUrl");
//    private static String quoteRequestUri = StreamsReader.getValue("quoteRequestStream.uri");
//    private static WebClient webClient;
//
//
//    public static void subscribeToQuoteRequestStreams() {
//        if (webClient == null) {
//            webClient =
//                    WebClient
//                            .builder()
//                            .baseUrl(quoteRequestBaseUrl)
//                            .defaultHeader("Content-Type", APPLICATION_STREAM_JSON_VALUE)
//                            .build();
//        }
//        startSubscriptionToQuoteRequestStreams("quoteRequest");
//    }
//
//    private static void startSubscriptionToQuoteRequestStreams(String streamName) {
//        System.out.println(format("establishing %s stream \n", streamName));
//        Executors.newCachedThreadPool().execute(() -> {
//            webClient.get()
//                    .uri(quoteRequestUri)
//                    .accept(MediaType.APPLICATION_STREAM_JSON)
//                    .exchange()
//                    .flatMapMany(response -> response.bodyToFlux(Map.class))
//                    .publishOn(Schedulers.parallel())
//                    .subscribe(
//                            body -> {
//
//                                if (body.containsKey("createdQuoteRequestEvent")) {
//
////                                    System.out.println("body quote request: " + body);
//                                    final Map<String, Object> event = (Map<String, Object>) body.get("createdQuoteRequestEvent");
//                                    if (event != null) {
////                                        System.out.println(format("Quote stream received: %s", event));
//                                        try {
//                                            event.put("name", "createdQuoteRequestEvent");
//                                            StreamWorldHelper.addQuoteRequestUpdates(event);
//                                        } catch (JsonProcessingException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//                                if (body.containsKey("expiringQuoteRequestEvent")) {
////                                    System.out.println("expiringQuoteRequestEvent body is = " +body);
//                                    final Map<String, Object> event = (Map<String, Object>) body.get("expiringQuoteRequestEvent");
//                                    if (event != null) {
////                                        System.out.println(format("Quote stream received: %s", event));
//                                        try {
//                                            event.put("name", "expiringQuoteRequestEvent");
//                                            StreamWorldHelper.addQuoteRequestUpdates(event);
//                                        } catch (JsonProcessingException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
////                                    }
//                                }
//                            },
//                            err -> {
//                                System.err.println(String.format("Failed to retrieve payload from quote request stream: %s", err));
//                            },
//                            () -> System.out.println("Quote stream stopped"));
//        });
//    }
}
