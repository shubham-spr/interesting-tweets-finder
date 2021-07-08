package com.listener.interestingtweetsfinder.repository;

import com.listener.interestingtweetsfinder.RedisSchema;
import com.listener.interestingtweetsfinder.model.ReferencedTweet;
import com.listener.interestingtweetsfinder.model.Tweet;
import com.listener.interestingtweetsfinder.service.PatternMatchingService;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class RedisFeedRepositoryImp implements RedisFeedRepository {

    private static final int DEFAULT_RECENT_TWEETS = 10;
    private static final int MAX_STREAM_SIZE = 100;
    static private final long INTERESTING_KEY_EXPIRATION_SECS =  60 * 60 * 24 * 10 * 1000; // 10 Days ms
    private static final int DEL_KEYS_AFTER_TWEET_COUNT = 1000;

    private final JedisPool jedisPool;
    private final PatternMatchingService patternMatchingService;
    private final AtomicLong counter;

    public RedisFeedRepositoryImp(JedisPool jedisPool, PatternMatchingService patternMatchingService){
        this.jedisPool=jedisPool;
        this.patternMatchingService=patternMatchingService;
        counter= new AtomicLong (0);
    }

    @Override
    public void addInterestingTweet(Tweet tweet, List<String> reasons) {
        String globalInterestingSetKey = RedisSchema.getInterestingHashKey ();
        long currentTime = System.currentTimeMillis ();
        boolean checkToDel = (counter.incrementAndGet ()%DEL_KEYS_AFTER_TWEET_COUNT==0);
        try(Jedis jedis = jedisPool.getResource ()){
            Pipeline p = jedis.pipelined ();
            p.zadd (globalInterestingSetKey, currentTime, tweet.getId());
            if(checkToDel){
                p.zremrangeByScore (globalInterestingSetKey,0,currentTime -  INTERESTING_KEY_EXPIRATION_SECS);
            }
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
                Double score = jedis.zscore(globalInterestingSetKey,referencedTweets.get (0).getId ());
                if(score!=null) return true;
            }
        }
        return false;
    }
}
