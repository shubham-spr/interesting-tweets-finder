package com.listener.interestingtweetsfinder.service;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaTweetProducer {

    private static final Logger logger = LoggerFactory.getLogger (KafkaTweetProducer.class);

    @Value ("${general.kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaTweetProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        ListenableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(topic, message);
        future.addCallback(new ListenableFutureCallback<> () {

            @Override
            public void onFailure(@NotNull Throwable ex) {
                logger.info("Unable to send message=[ {} ] due to : {}", message, ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                logger.info("Sent message=[ {} ] with offset=[ {} ]", message, result.getRecordMetadata().offset());
            }

        });
    }

}
