package com.aixtrade.streams.order;

import com.aixtrade.streams.kafka.KafkaStreamEventPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrderStreamResponse extends OrderStreamData implements KafkaStreamEventPayload {

    public static final Comparator<OrderStreamResponse> ORDER_BY_ENTERED_AT_MILLIS =
            Comparator.comparing(OrderStreamResponse::getEnteredAtInMillis);

    public static final Comparator<OrderStreamResponse> ORDER_BY_UPDATED_AT_MILLIS =
            Comparator.comparing(OrderStreamResponse::getUpdatedAtInMillis);

    private String id;
}
