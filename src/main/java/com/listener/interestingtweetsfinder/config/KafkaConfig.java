package com.listener.interestingtweetsfinder.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value ("${spring.kafka.consumer.bootstrap-servers}")
    private String consumerAddress;

    @Value ("${spring.kafka.producer.bootstrap-servers}")
    private String producerAddress;

    @Value ("${general.kafka.topic}")
    private String topic;

    @Bean
    public NewTopic generalTopic(@Value ("${general.kafka.mongo_consumers.size}") int numConsumers) {
        return TopicBuilder.name(topic)
                .partitions(numConsumers)
                .replicas(1)
                .build();
    }

    @Bean
    public ProducerFactory<String, String>  producerFactory(){
        Map<String,Object> configProps = new HashMap<> ();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<> (configProps);
    }

    @Bean
    public KafkaTemplate<String,String> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory ());
    }

    @Bean
    public ConsumerFactory<String,String> consumerFactory(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerAddress);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        return new DefaultKafkaConsumerFactory<> (configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setRetryTemplate (retryTemplate ());
        return factory;
    }

    @Bean
    public RetryTemplate retryTemplate(){
        RetryTemplate retryTemplate = new RetryTemplate ();

        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy ();
        exponentialBackOffPolicy.setInitialInterval (2000);
        exponentialBackOffPolicy.setMultiplier (2);
        exponentialBackOffPolicy.setMaxInterval (20000);
        retryTemplate.setBackOffPolicy (exponentialBackOffPolicy);

        SimpleRetryPolicy simpleRetryPolicy= new SimpleRetryPolicy ();
        simpleRetryPolicy.setMaxAttempts (3);
        retryTemplate.setRetryPolicy (simpleRetryPolicy);

        return retryTemplate;
    }
}
