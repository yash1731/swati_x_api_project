package com.aixtrade.streams.kafka;

import com.aixtrade.orderControl.OrderIdControl;
import com.aixtrade.streams.order.OrderStreamResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Arrays.asList;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderKafkaEventVerifier extends BaseKafkaEventVerifier<OrderStreamResponse> {

    private static final String ORDER_ENTITY = "order";
    private static final String TWO_WAY_ORDER_ENTITY = "twoWayOrder";
    private static final String CANCELLED_BULK_ORDER_TYPE = "cancelledBulkOrder";
    private static final List<String> OPEN_ORDER_STATUSES = asList("Open", "PartiallyExecuted");

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final OrderIdControl orderIdControl;

    @KafkaListener(topics = {"${orderService.kafka.order.topic}"}, containerFactory = "orderKafkaListenerContainerFactory", properties = {"auto.offset.reset=latest"})
    public void onMessageReceived(@Payload KafkaStreamEvent<OrderStreamResponse> kafkaStreamEvent, @Headers MessageHeaders headers) {

        executorService.submit(() -> {
            log.info("Received order kafka event: {}", kafkaStreamEvent);

            final OrderStreamResponse orderStreamResponse = kafkaStreamEvent.getPayload();
            final String entity = kafkaStreamEvent.getEntity();
            final String type = kafkaStreamEvent.getType();

            if (ORDER_ENTITY.equals(entity)) {

                if (CANCELLED_BULK_ORDER_TYPE.equals(type)) {
//                    convertToBulkOrderStreamResponse(payload).ifPresent(bulkOrderStreamResponses::add);
                } else {
                    add(orderStreamResponse);
                }

                updateOrderOrderId(orderStreamResponse);
            }
        });
    }

    @PreDestroy
    public void shutdown() {
        shutDownExecutor(executorService);
    }

    private void updateOrderOrderId(OrderStreamResponse orderStreamResponse) {
        final String orderId = orderStreamResponse.getId();

        if (OPEN_ORDER_STATUSES.contains(orderStreamResponse.getOrderStatus())) {
            orderIdControl.storeOrderId(orderId);
        }
    }
}
