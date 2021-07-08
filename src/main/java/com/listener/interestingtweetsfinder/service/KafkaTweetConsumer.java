package com.listener.interestingtweetsfinder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaTweetConsumer {

    private static final Logger logger = LoggerFactory.getLogger (KafkaTweetConsumer.class);

    @KafkaListener(
            topics = "${general.kafka.topic}",
            concurrency = "${general.kafka.consumer_group_size}",
            groupId = "${spring.kafka.consumer.group-id}")
//    @RetryableTopic(attempts = "2", backoff = @Backoff(delay = 2000, maxDelay = 10000, multiplier = 2))
        public void consume(String message) {
        logger.info(String.format("Consumed message: %s", message));
    }

//    @KafkaListener(topics = "${general.kafka.topic}", groupId = "${spring.kafka.consumer.group-id-2}")
//    @KafkaListener(topics = "${general.kafka.topic}", groupId = "${spring.kafka.consumer.group-id-2}")
//    public void consume(String message) {
//        logger.info(String.format("Consumed message: %s", message));
//    }

}
