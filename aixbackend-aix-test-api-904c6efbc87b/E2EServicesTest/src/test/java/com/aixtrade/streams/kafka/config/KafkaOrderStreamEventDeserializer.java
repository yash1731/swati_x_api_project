package com.aixtrade.streams.kafka.config;

import com.aixtrade.streams.kafka.KafkaStreamEvent;
import com.aixtrade.streams.order.OrderStreamResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.io.IOException;
import java.util.Arrays;

class KafkaOrderStreamEventDeserializer<T extends OrderStreamResponse> extends JsonDeserializer<KafkaStreamEvent<T>> {

    @Override
    public KafkaStreamEvent<T> deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(topic, data);
    }

    @Override
    public KafkaStreamEvent<T> deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.readValue(data, new TypeReference<KafkaStreamEvent<T>>() {
            });
        } catch (IOException e) {
            throw new SerializationException("Can't deserialize data [" + Arrays.toString(data) +
                    "] from topic [" + topic + "]", e);
        }
    }
}
