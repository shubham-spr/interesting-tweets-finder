package com.listener.interestingtweetsfinder.controller;

import com.listener.interestingtweetsfinder.repository.RedisFeedRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TweetFeedController {

    private final RedisFeedRepository redisFeedRepository;

    public TweetFeedController(RedisFeedRepository redisFeedRepository){
        this.redisFeedRepository=redisFeedRepository;
    }

    @GetMapping("recent")
    public Map<String, List<Map<String,String>>> getRecentInterestingTweets(@RequestParam("limit") int limit){
        return redisFeedRepository.getMostRecentInterestingTweets (limit);
    }

    @GetMapping("recent")
    public Map<String, List<Map<String,String>>> getRecentInterestingTweets(){
        return redisFeedRepository.getMostRecentInterestingTweets ();
    }

    @GetMapping("recent/{reason}")
    public List<Map<String,String>> getRecentInterestingTweetsByReason(@PathVariable String reason){
        return redisFeedRepository.getMostRecentInterestingTweets (reason);
    }

    @GetMapping("recent/{reason}")
    public List<Map<String,String>> getRecentInterestingTweetsByReason(
            @PathVariable String reason,
            @RequestParam("limit") int limit){
        return redisFeedRepository.getMostRecentInterestingTweets (reason,limit);
    }

    @GetMapping("tweet/{convId}")
    public Map<String, String> getConversation(@PathVariable String convId){
        return null;
    }

}
