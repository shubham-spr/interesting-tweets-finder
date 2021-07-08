package com.listener.interestingtweetsfinder.repository;

import com.listener.interestingtweetsfinder.model.Tweet;

import java.util.List;
import java.util.Map;

public interface RedisFeedRepository {
    
    /**
     * Adds the tweet to redis.
     * Also: See {@link com.listener.interestingtweetsfinder.RedisSchema}
     *
     * @param tweet tweet that will be saved int the redis
     * @param reason the regex id that makes the tweet interesting
     */
    void addInterestingTweet(Tweet tweet,List<String> reason);

    /**
     * Returns the top most recent interesting tweets for all regex-ids
     *
     * @param limit the number of tweets to return for all regex-id (reason)
     */
    Map<String,List<Map<String,String>>> getMostRecentInterestingTweets(int limit);
    Map<String,List<Map<String,String>>> getMostRecentInterestingTweets();

    /**
     * Returns the top most recent interesting tweets for a particular regex id
     *
     * @param reason the regex-id
     */
    List<Map<String,String>> getMostRecentInterestingTweets(String reason);
    List<Map<String,String>> getMostRecentInterestingTweets(String reason,int limit);

    boolean isChildOfInteresting(Tweet tweet);

}
