package com.aixtrade.streams.quote;


import com.aixtrade.streams.kafka.KafkaStreamEventPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QuoteStreamResponse extends QuoteStreamData implements KafkaStreamEventPayload {
}
