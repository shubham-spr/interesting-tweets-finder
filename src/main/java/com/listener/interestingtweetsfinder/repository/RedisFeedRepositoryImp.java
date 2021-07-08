package com.listener.interestingtweetsfinder.repository;

import com.listener.interestingtweetsfinder.RedisSchema;
import com.listener.interestingtweetsfinder.model.ReferencedTweet;
import com.listener.interestingtweetsfinder.model.Tweet;
import com.listener.interestingtweetsfinder.service.PatternMatchingService;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.*;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RedisFeedRepositoryImp implements RedisFeedRepository {

    private static final int DEFAULT_RECENT_TWEETS = 10;
    private static final int MAX_STREAM_SIZE = 100;

    private final JedisPool jedisPool;
    private final PatternMatchingService patternMatchingService;

    public RedisFeedRepositoryImp(JedisPool jedisPool, PatternMatchingService patternMatchingService){
        this.jedisPool=jedisPool;
        this.patternMatchingService=patternMatchingService;
    }

    @Override
    public void addInterestingTweet(Tweet tweet, List<String> reasons) {
        String globalInterestingSetKey = RedisSchema.getInterestingHashKey ();
        try(Jedis jedis = jedisPool.getResource ()){
            Pipeline p = jedis.pipelined ();
            p.sadd (globalInterestingSetKey, tweet.getId ());
            for(String reason: reasons){
                p.xadd (
                        RedisSchema.getInterestingHashKey (reason),
                        StreamEntryID.NEW_ENTRY,
                        tweet.toMap (),
                        MAX_STREAM_SIZE,
                        true
                );
            }
            p.sync ();
        }
    }

    @Override
    public Map<String,List<Map<String,String>>> getMostRecentInterestingTweets() {
        return getMostRecentInterestingTweets (DEFAULT_RECENT_TWEETS);
    }

    @Override
    public Map<String,List<Map<String,String>>> getMostRecentInterestingTweets(int limit) {
        Set<String> reasons = patternMatchingService.getInterestingReasonIds ();
        Map<String,List<Map<String,String>>> res  = new HashMap<> ();
        try(Jedis jedis= jedisPool.getResource ()){
            for(String reason: reasons){
                List<StreamEntry> response =
                        jedis.xrevrange(RedisSchema.getInterestingHashKey (reason), null, null, limit);
                res.put (reason,response
                        .stream ()
                        .map (StreamEntry::getFields)
                        .collect(Collectors.toList())
                );
            }
        }
        return res;
    }

    @Override
    public List<Map<String,String>> getMostRecentInterestingTweets(String reason) {
        return getMostRecentInterestingTweets (reason,DEFAULT_RECENT_TWEETS);
    }

    @Override
    public List<Map<String,String>> getMostRecentInterestingTweets(String reason, int limit) {
        try(Jedis jedis= jedisPool.getResource ()) {
            List<StreamEntry> response =
                    jedis.xrevrange(RedisSchema.getInterestingHashKey (reason), null, null, limit);
            return response.stream ()
                    .map (StreamEntry::getFields)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean isChildOfInteresting(Tweet tweet) {
        String globalInterestingSetKey = RedisSchema.getInterestingHashKey ();
        try(Jedis jedis = jedisPool.getResource ()){
            List<ReferencedTweet> referencedTweets = tweet.getReferencedTweets ();
            if(referencedTweets!=null){
                return jedis.sismember (globalInterestingSetKey,referencedTweets.get (0).getId ());
            }
        }
        return false;
    }
}
