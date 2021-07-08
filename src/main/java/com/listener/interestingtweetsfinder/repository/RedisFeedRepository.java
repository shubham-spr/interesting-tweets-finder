package com.listener.interestingtweetsfinder.repository;

import com.listener.interestingtweetsfinder.model.Tweet;

import java.util.List;
import java.util.Map;

public interface RedisFeedRepository {

    // interesting:[tw1,tw2,tw3,...]
    // interesting:reason [twid1|twid2|twid3..] (a stream of tweet ids)
    void addInterestingTweet(Tweet tweet,List<String> reason);

    // {reason -> {top 10}}
    Map<String,List<Map<String,String>>> getMostRecentInterestingTweets(int limit);
    Map<String,List<Map<String,String>>> getMostRecentInterestingTweets();

    List<Map<String,String>> getMostRecentInterestingTweets(String reason);
    List<Map<String,String>> getMostRecentInterestingTweets(String reason,int limit);

    // check if parent id is in interesting
    boolean isChildOfInteresting(Tweet tweet);

}
