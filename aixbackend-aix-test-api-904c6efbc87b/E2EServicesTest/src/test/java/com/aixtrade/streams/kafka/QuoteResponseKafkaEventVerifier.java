package com.aixtrade.streams.kafka;

import com.aixtrade.quoteControl.QuoteResponseIdControl;
import com.aixtrade.streams.quoteresponse.QuoteResponseStreamResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuoteResponseKafkaEventVerifier extends BaseKafkaEventVerifier<QuoteResponseStreamResponse> {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final QuoteResponseIdControl quoteResponseIdControl;

    @KafkaListener(topics = {"${quoteService.kafka.quoteresponse.topic}"}, containerFactory = "quoteResponseKafkaListenerContainerFactory", properties = {"auto.offset.reset=latest"})
    public void onMessageReceived(@Payload KafkaStreamEvent<QuoteResponseStreamResponse> kafkaStreamEvent, @Headers MessageHeaders headers) {

        executorService.submit(() -> {
            log.info("Received quote response kafka event: {}", kafkaStreamEvent);
            final QuoteResponseStreamResponse quoteResponseStreamResponse = kafkaStreamEvent.getPayload();
            final String eventType = kafkaStreamEvent.getType();
            quoteResponseStreamResponse.setName(eventType);
            add(quoteResponseStreamResponse);
            updateQuoteResponseId(quoteResponseStreamResponse);
        });
    }

    @PreDestroy
    public void shutdown() {
        shutDownExecutor(executorService);
    }

    private void updateQuoteResponseId(QuoteResponseStreamResponse quoteResponseStreamResponse) {
        final Long quoteRequestId = quoteResponseStreamResponse.getQuoteResponseId();
        quoteResponseIdControl.storeQuoteResponseId(quoteRequestId.intValue());
    }
}
