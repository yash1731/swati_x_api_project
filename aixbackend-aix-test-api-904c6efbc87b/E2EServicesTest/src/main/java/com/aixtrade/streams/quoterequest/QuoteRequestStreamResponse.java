package com.aixtrade.streams.quoterequest;

import com.aixtrade.streams.kafka.KafkaStreamEventPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QuoteRequestStreamResponse extends com.aixtrade.streams.quoterequest.QuoteRequestStreamData implements KafkaStreamEventPayload {
}
