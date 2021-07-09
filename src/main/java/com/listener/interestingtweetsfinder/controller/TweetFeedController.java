package com.listener.interestingtweetsfinder.controller;

import com.listener.interestingtweetsfinder.repository.RedisFeedRepository;
import com.listener.interestingtweetsfinder.repository.TweetRepository;
import com.listener.interestingtweetsfinder.service.PatternMatchingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class TweetFeedController {

    private final RedisFeedRepository redisFeedRepository;
    private final TweetRepository tweetRepository;
    private final PatternMatchingService patternMatchingService;

    public TweetFeedController(
            RedisFeedRepository redisFeedRepository,
            TweetRepository tweetRepository,
            PatternMatchingService patternMatchingService){
        this.redisFeedRepository=redisFeedRepository;
        this.tweetRepository=tweetRepository;
        this.patternMatchingService=patternMatchingService;
    }

    @GetMapping("recent")
    public String getRecentInterestingTweets(
            @RequestParam("limit") int limit,
            Model model){
        model.addAttribute ("allInterestingTweets",redisFeedRepository.getMostRecentInterestingTweets (limit));
        return "index";
    }

    @GetMapping("recent/{reason}")
    public String getRecentInterestingTweetsByReason(
            @PathVariable String reason,
            @RequestParam("limit") int limit,
            Model model){
        model.addAttribute ("tweetsForReason",redisFeedRepository.getMostRecentInterestingTweets (reason,
                limit));
        return "index";
    }

    @GetMapping("conversation/{convId}")
    public String getConversation(@PathVariable String convId, Model model){
        model.addAttribute ("tweets",tweetRepository.findTweetsByConversationId (convId));
        return "index";
    }

    @GetMapping("reasons")
    public String getReasons(Model model){
        model.addAttribute ("reasons",patternMatchingService.getInterestingReasonIds ());
        return "index";
    }

//    @GetMapping("tweet/{tweetId}")
//    public Tweet getTweet(@PathVariable String tweetId){
//        return tweetRepository.findTweetById (tweetId);
//    }

}

