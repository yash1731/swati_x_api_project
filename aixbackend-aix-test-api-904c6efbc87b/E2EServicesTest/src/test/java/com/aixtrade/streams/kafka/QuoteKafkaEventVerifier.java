package com.aixtrade.streams.kafka;

import com.aixtrade.quoteControl.QuoteIdControl;
import com.aixtrade.streams.quote.QuoteStreamResponse;
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
public class QuoteKafkaEventVerifier extends BaseKafkaEventVerifier<QuoteStreamResponse> {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final QuoteIdControl quoteIdControl;

    @KafkaListener(
            topics = {"${quoteService.kafka.quote.topic}"},
            containerFactory = "quoteKafkaListenerContainerFactory",
            properties = {"auto.offset.reset=latest"})
    public void onMessageReceived(@Payload KafkaStreamEvent<QuoteStreamResponse> kafkaStreamEvent, @Headers MessageHeaders headers) {

        executorService.submit(() -> {
            log.info("Received quote kafka event: {}", kafkaStreamEvent);
            final QuoteStreamResponse quoteStreamResponse = kafkaStreamEvent.getPayload();
            final String eventType = kafkaStreamEvent.getType();
            quoteStreamResponse.setName(eventType);
            add(quoteStreamResponse);
            updateQuoteId(quoteStreamResponse);
        });
    }

    @PreDestroy
    public void shutdown() {
        shutDownExecutor(executorService);
    }

    private void updateQuoteId(QuoteStreamResponse quoteStreamResponse) {
        final Long quoteRequestId = quoteStreamResponse.getQuoteResponseId();
        quoteIdControl.storeQuoteId(quoteRequestId.intValue());
    }
}
