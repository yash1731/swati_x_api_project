package com.aixtrade.streams.order;

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


public class OrderStreamFactory extends BaseStreamFactory{

//    private static String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
//    private static String orderUri = StreamsReader.getValue("orderStream.uri");
//    private static WebClient webClient;
//
//
//    public static void subscribeToOrderbookStreams() {
//        if (webClient == null) {
//            webClient =
//                    WebClient
//                            .builder()
//                            .baseUrl(orderBaseUrl)
//                            .defaultHeader("Content-Type", APPLICATION_STREAM_JSON_VALUE)
//                            .build();
//        }
//        startSubscriptionToOrderbookStreams("order");
//    }
//
//    private static void startSubscriptionToOrderbookStreams(String streamName) {
//        System.out.println(format("establishing %s stream \n", streamName));
//        Executors.newCachedThreadPool().execute(() -> {
//            webClient.get()
//                    .uri(orderUri)
//                    .accept(MediaType.APPLICATION_STREAM_JSON)
//                    .exchange()
//                    .flatMapMany(response -> response.bodyToFlux(Map.class))
//                    .publishOn(Schedulers.parallel())
//                    .subscribe(
//                            body -> {
//
//                                if (body.containsKey("event")) {
//
////                                    System.out.println("body order: " + body);
//
//                                    final Map<String, Object> event = (Map<String, Object>) body.get("event");
//
//                                    final Map<String, Object> payload = (Map<String, Object>) event.get("payload");
//
//                                    if (payload != null) {
////                                        System.out.println(format("OrderData stream received: %s", payload));
//                                        try {
//                                            StreamWorldHelper.addOrderUpdates(payload);
//                                        } catch (JsonProcessingException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//                            },
//                            err -> {
//                                System.err.println(String.format("Failed to retrieve payload from order stream, error: %s", err));
//                            },
//                            () -> System.out.println("OrderData stream stopped"));
//        });
//    }
}
