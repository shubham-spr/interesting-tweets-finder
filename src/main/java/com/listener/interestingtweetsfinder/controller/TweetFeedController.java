package com.listener.interestingtweetsfinder.controller;

import com.listener.interestingtweetsfinder.model.Tweet;
import com.listener.interestingtweetsfinder.repository.RedisFeedRepository;
import com.listener.interestingtweetsfinder.repository.TweetRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TweetFeedController {

    private final RedisFeedRepository redisFeedRepository;
    private final TweetRepository tweetRepository;

    public TweetFeedController(
            RedisFeedRepository redisFeedRepository,
            TweetRepository tweetRepository){
        this.redisFeedRepository=redisFeedRepository;
        this.tweetRepository=tweetRepository;
    }

    @GetMapping("recent")
    public Map<String, List<Map<String,String>>> getRecentInterestingTweets(@RequestParam("limit") int limit){
        return redisFeedRepository.getMostRecentInterestingTweets (limit);
    }

    @GetMapping("recent/{reason}")
    public List<Map<String,String>> getRecentInterestingTweetsByReason(
            @PathVariable String reason,
            @RequestParam("limit") int limit){
        return redisFeedRepository.getMostRecentInterestingTweets (reason,limit);
    }

    @GetMapping("conversation/{convId}")
    public List<Tweet> getConversation(@PathVariable String convId){
        return tweetRepository.findTweetsByConversationId (convId);
    }

    @GetMapping("tweet/{tweetId}")
    public Tweet getTweet(@PathVariable String tweetId){
        return tweetRepository.findTweetById (tweetId);
    }

}
