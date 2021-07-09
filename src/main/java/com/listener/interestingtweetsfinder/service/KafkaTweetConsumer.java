package com.listener.interestingtweetsfinder.service;

import com.listener.interestingtweetsfinder.model.StreamElement;
import com.listener.interestingtweetsfinder.model.Tweet;
import com.listener.interestingtweetsfinder.repository.RedisFeedRepository;
import com.listener.interestingtweetsfinder.repository.RedisFeedRepositoryImp;
import com.listener.interestingtweetsfinder.repository.TweetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class KafkaTweetConsumer {

    private static final Logger logger = LoggerFactory.getLogger (KafkaTweetConsumer.class);
    private static final long STATS_AFTER_NUM_TWEETS = 100;

    private final PatternMatchingService patternMatchingService;
    private final RedisFeedRepository redisFeedRepository;
    private final TweetRepository tweetRepository;
    private final AtomicLong mongoConsumerCounter;
    private final AtomicLong redisConsumerCounter;

    public KafkaTweetConsumer(
            RedisFeedRepositoryImp repository,
            PatternMatchingService patternMatchingService,
            TweetRepository tweetRepository){
        this.redisFeedRepository =repository;
        this.patternMatchingService=patternMatchingService;
        this.tweetRepository=tweetRepository;
        mongoConsumerCounter = new AtomicLong (0);
        redisConsumerCounter = new AtomicLong (0);
    }

    @KafkaListener(
            topics = "${general.kafka.topic}",
            concurrency = "${general.kafka.mongo_consumers.size}",
            groupId = "${general.kafka.mongo_consumers.group_id}")
    public void consumeStreamedTweetsIfInteresting(String message) {
        Optional<StreamElement> optional = StreamElement.fromString (message);
        if(optional.isEmpty ()) return;
        StreamElement element = optional.get ();
        Tweet tweet= element.getData ();
        List<String> reason = patternMatchingService.findMatchingRegexIdsForText (tweet.getText ());
        if(reason.size ()>0) {
            redisFeedRepository.addInterestingTweet (tweet,reason);
            tweetRepository.save (tweet);
        }
        if(mongoConsumerCounter.incrementAndGet ()%STATS_AFTER_NUM_TWEETS==0){
            logger.info ("Mongo Consumers Consumed: "+mongoConsumerCounter.get ()+" Tweets ");
        }
    }

    @KafkaListener(
            topics = "${general.kafka.topic}",
            concurrency = "${general.kafka.redis_consumers.size}",
            groupId = "${general.kafka.redis_consumers.group_id}")
    public void consumeIfInterestingParent(String message) {
        Optional<StreamElement> optional = StreamElement.fromString (message);
        if(optional.isEmpty ()) return;
        StreamElement element = optional.get ();
        Tweet tweet= element.getData ();
        if(redisFeedRepository.isChildOfInteresting (tweet)){
            logger.info (tweet.getText ()+ " has parent tweet interesting");
            tweetRepository.save (tweet);
        }
        if(redisConsumerCounter.incrementAndGet ()%STATS_AFTER_NUM_TWEETS==0){
            logger.info ("Redis Consumers Consumed: "+redisConsumerCounter.get ()+" Tweets");
        }
    }

}
