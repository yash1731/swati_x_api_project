package com.aixtrade.streams.kafka;

import com.aixtrade.quoteControl.QuoteRequestIdControl;
import com.aixtrade.streams.quoterequest.QuoteRequestStreamResponse;
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
public class QuoteRequestKafkaEventVerifier extends BaseKafkaEventVerifier<QuoteRequestStreamResponse> {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final QuoteRequestIdControl quoteRequestIdControl;

    @KafkaListener(topics = {"${quoteService.kafka.quoterequest.topic}"}, containerFactory = "quoteRequestKafkaListenerContainerFactory", properties = {"auto.offset.reset=latest"})
    public void onMessageReceived(@Payload KafkaStreamEvent<QuoteRequestStreamResponse> kafkaStreamEvent, @Headers MessageHeaders headers) {

        executorService.submit(() -> {
            log.info("Received quote request kafka event: {}", kafkaStreamEvent);
            final QuoteRequestStreamResponse quoteRequestStreamResponse = kafkaStreamEvent.getPayload();
            final String eventType = kafkaStreamEvent.getType();
            quoteRequestStreamResponse.setName(eventType);
            add(quoteRequestStreamResponse);
            updateQuoteRequestId(quoteRequestStreamResponse);
        });
    }

    @PreDestroy
    public void shutdown() {
        shutDownExecutor(executorService);
    }

    private void updateQuoteRequestId(QuoteRequestStreamResponse quoteRequestStreamResponse) {
        final Long quoteRequestId = quoteRequestStreamResponse.getQuoteRequestId();
        quoteRequestIdControl.storeQuoteRequestId(quoteRequestId.intValue());
    }
}
