package com.listener.interestingtweetsfinder.service;

import com.listener.interestingtweetsfinder.model.StreamElement;
import com.listener.interestingtweetsfinder.model.Tweet;
import com.listener.interestingtweetsfinder.repository.TweetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KafkaTweetConsumer {

    private static final Logger logger = LoggerFactory.getLogger (KafkaTweetConsumer.class);

    private final TweetRepository tweetRepository;
    private final PatternMatchingService patternMatchingService;

    public KafkaTweetConsumer(TweetRepository repository, PatternMatchingService patternMatchingService){
        this.tweetRepository=repository;
        this.patternMatchingService=patternMatchingService;
    }

    //    @RetryableTopic(attempts = "2", backoff = @Backoff(delay = 2000, maxDelay = 10000, multiplier = 2))
    @KafkaListener(topics = "${general.kafka.topic}", concurrency = "${general.kafka.consumer_group_size}",groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        // logger.info ("received {}",message);
        Optional<StreamElement> optional = StreamElement.fromString (message);
        if(optional.isEmpty ()) return;
        StreamElement element = optional.get ();
        Tweet tweet= element.getData ();
        tweetRepository.save (tweet);
        List<String> matchIds = patternMatchingService.findMatchingRegexIdsForText (tweet.getText ());
        if(matchIds.size ()>0) {
            logger.info ("For "+matchIds+" : "+tweet.getText ());
        }
    }

//    @KafkaListener(topics = "${general.kafka.topic}", groupId = "${spring.kafka.consumer.group-id-2}")
//    @KafkaListener(topics = "${general.kafka.topic}", groupId = "${spring.kafka.consumer.group-id-2}")
//    public void consume(String message) {
//        logger.info(String.format("Consumed message: %s", message));
//    }

}
