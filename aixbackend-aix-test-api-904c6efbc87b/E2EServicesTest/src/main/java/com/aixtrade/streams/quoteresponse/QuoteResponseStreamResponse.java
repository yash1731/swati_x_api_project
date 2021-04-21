package com.aixtrade.streams.quoteresponse;


import com.aixtrade.streams.kafka.KafkaStreamEventPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QuoteResponseStreamResponse extends QuoteResponseStreamData implements KafkaStreamEventPayload {
}
