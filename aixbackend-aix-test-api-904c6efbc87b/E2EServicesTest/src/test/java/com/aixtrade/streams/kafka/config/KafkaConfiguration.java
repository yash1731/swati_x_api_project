package com.aixtrade.streams.kafka.config;

import com.aixtrade.streams.kafka.KafkaStreamEvent;
import com.aixtrade.streams.order.OrderStreamResponse;
import com.aixtrade.streams.quote.QuoteStreamResponse;
import com.aixtrade.streams.quoterequest.QuoteRequestStreamResponse;
import com.aixtrade.streams.quoteresponse.QuoteResponseStreamResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@EnableKafka
@Configuration
public class KafkaConfiguration {

    private static final long MAX_KAFKA_READY_WAIT_IN_SECS = 60;
    // todo:  adding unique client-id until we find a better way to seek to the end of kafka offset
//    private static final String CONSUMER_GROUP_ID = "end2endtest-client-id" + System.nanoTime();
    private static final String CONSUMER_GROUP_ID = "end2endtest-client-id3";


    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public Map<String, Object> consumerConfigs() {

        final Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return props;
    }

    @Bean("orderKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, KafkaStreamEvent<OrderStreamResponse>> orderKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaStreamEvent<OrderStreamResponse>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());
//        factory.setRecordFilterStrategy(consumerRecord -> "LTCUSDT".equals(consumerRecord.value().getPayload().getSymbol()));
        factory.setAutoStartup(true);
        return factory;
    }

    private ConsumerFactory<String, KafkaStreamEvent<OrderStreamResponse>> orderConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new KafkaOrderStreamEventDeserializer<>());
    }

    @Bean("quoteRequestKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, KafkaStreamEvent<QuoteRequestStreamResponse>> quoteRequestKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaStreamEvent<QuoteRequestStreamResponse>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(quoteRequestConsumerFactory());
//        factory.setRecordFilterStrategy(consumerRecord -> "LTCUSDT".equals(consumerRecord.value().getPayload().getSymbol()));
        factory.setAutoStartup(true);
        return factory;
    }

    private ConsumerFactory<String, KafkaStreamEvent<QuoteRequestStreamResponse>> quoteRequestConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new KafkaQuoteRequestStreamEventDeserializer<>());
    }

    @Bean("quoteResponseKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, KafkaStreamEvent<QuoteResponseStreamResponse>> quoteResponseKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaStreamEvent<QuoteResponseStreamResponse>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(quoteResponseConsumerFactory());
//        factory.setRecordFilterStrategy(consumerRecord -> "LTCUSDT".equals(consumerRecord.value().getPayload().getSymbol()));
        factory.setAutoStartup(true);
        return factory;
    }

    private ConsumerFactory<String, KafkaStreamEvent<QuoteResponseStreamResponse>> quoteResponseConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new KafkaQuoteResponseStreamEventDeserializer<>());
    }

    @Bean("quoteKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, KafkaStreamEvent<QuoteStreamResponse>> quoteKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaStreamEvent<QuoteStreamResponse>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(quoteConsumerFactory());
//        factory.setRecordFilterStrategy(consumerRecord -> "LTCUSDT".equals(consumerRecord.value().getPayload().getSymbol()));
        factory.setAutoStartup(true);
        return factory;
    }

    private ConsumerFactory<String, KafkaStreamEvent<QuoteStreamResponse>> quoteConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new KafkaQuoteStreamEventDeserializer<>());
    }
}
