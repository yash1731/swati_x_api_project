package com.aixtrade.streams.quoteresponse;

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

public class QuoteResponseStreamFactory {

//    private static String quoteRequestBaseUrl = EnvConfig.getValue("quoteService baseUrl");
//    private static String quoteResponseUri = StreamsReader.getValue("quoteResponseStream.uri");
//    private static WebClient webClient;
//
//
//    public static void subscribeToQuoteResponseStreams() {
//        if (webClient == null) {
//            webClient =
//                    WebClient
//                            .builder()
//                            .baseUrl(quoteRequestBaseUrl)
//                            .defaultHeader("Content-Type", APPLICATION_STREAM_JSON_VALUE)
//                            .build();
//        }
//        startSubscriptionToQuoteResponseStreams("quoteRequest");
//    }
//
//    private static void startSubscriptionToQuoteResponseStreams(String streamName) {
//        System.out.println(format("establishing %s stream \n", streamName));
//        Executors.newCachedThreadPool().execute(() -> {
//            webClient.get()
//                    .uri(quoteResponseUri)
//                    .accept(MediaType.APPLICATION_STREAM_JSON)
//                    .exchange()
//                    .flatMapMany(response -> response.bodyToFlux(Map.class))
//                    .publishOn(Schedulers.parallel())
//                    .subscribe(
//                            body -> {
////                                System.out.println(body);
//                                if (body.containsKey("createdQuoteResponseEvent")) {
//                                    final Map<String, Object> event = (Map<String, Object>) body.get("createdQuoteResponseEvent");
//                                    if (event != null) {
////                                        System.out.println(format("Quote stream received: %s", event));
//                                        try {
//                                            event.put("name", "createdQuoteResponseEvent");
//                                            StreamWorldHelper.addQuoteResponseUpdates(event);
//                                        } catch (JsonProcessingException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//                            },
//                            err -> {
//                                System.err.println(String.format("Failed to retrieve payload from quote response stream: %s", err));
//                            },
//                            () -> System.out.println("Quote stream stopped"));
//        });
//    }
}
