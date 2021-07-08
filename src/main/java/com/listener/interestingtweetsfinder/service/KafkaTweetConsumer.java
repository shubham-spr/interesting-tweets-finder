package com.listener.interestingtweetsfinder.service;

import com.listener.interestingtweetsfinder.model.StreamElement;
import com.listener.interestingtweetsfinder.model.Tweet;
import com.listener.interestingtweetsfinder.repository.RedisFeedRepository;
import com.listener.interestingtweetsfinder.repository.RedisFeedRepositoryImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KafkaTweetConsumer {

    private static final Logger logger = LoggerFactory.getLogger (KafkaTweetConsumer.class);

    private final PatternMatchingService patternMatchingService;
    private final RedisFeedRepository redisFeedRepository;

    public KafkaTweetConsumer(RedisFeedRepositoryImp repository, PatternMatchingService patternMatchingService){
        this.redisFeedRepository =repository;
        this.patternMatchingService=patternMatchingService;
    }

    @KafkaListener(topics = "${general.kafka.topic}",
            concurrency = "${general.kafka.mongo_consumers.size}",
            groupId = "${general.kafka.mongo_consumers.group_id}")
    public void consumeIfInteresting(String message) {
        Optional<StreamElement> optional = StreamElement.fromString (message);
        if(optional.isEmpty ()) return;
        StreamElement element = optional.get ();
        Tweet tweet= element.getData ();
        List<String> reason = patternMatchingService.findMatchingRegexIdsForText (tweet.getText ());
        if(reason.size ()>0) redisFeedRepository.addInterestingTweet (tweet,reason);
    }

    @KafkaListener(topics = "${general.kafka.topic}",
            concurrency = "${general.kafka.redis_consumers.size}",
            groupId = "${general.kafka.redis_consumers.group_id}")
    public void consumeIfInterestingParent(String message) {
        Optional<StreamElement> optional = StreamElement.fromString (message);
        if(optional.isEmpty ()) return;
        StreamElement element = optional.get ();
        Tweet tweet= element.getData ();
        if(redisFeedRepository.isChildOfInteresting (tweet)){
            logger.info (tweet.getText ()+ " has parent tweet interesting");
        }
    }

//    @RetryableTopic(attempts = "2", backoff = @Backoff(delay = 2000, maxDelay = 10000, multiplier = 2))
//    @KafkaListener(topics = "${general.kafka.topic}", groupId = "${spring.kafka.consumer.group-id-2}")
//    @KafkaListener(topics = "${general.kafka.topic}", groupId = "${spring.kafka.consumer.group-id-2}")
//    public void consume(String message) {
//        logger.info(String.format("Consumed message: %s", message));
//    }

}
