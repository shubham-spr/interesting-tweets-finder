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

    @GetMapping("index.html")
    public String greetings(){
        return "index";
    }

    /**
     * Get the most recent interesting tweets for each regex-id
     *
     * @param limit the amount of tweets to get for each regex-id
     */
    @GetMapping("recent")
    public String getRecentInterestingTweets(
            @RequestParam("limit") int limit,
            Model model){
        model.addAttribute ("allInterestingTweets",redisFeedRepository.getMostRecentInterestingTweets (limit));
        return "index";
    }

    /**
     * Get the most recent interesting tweets for a single regex
     *
     * @param reason the regex id
     * @param limit the amount of tweets to get for the corresponding regex
     */
    @GetMapping("recent/{reason}")
    public String getRecentInterestingTweetsByReason(
            @PathVariable String reason,
            @RequestParam("limit") int limit,
            Model model){
        model.addAttribute ("tweetsForReason",redisFeedRepository.getMostRecentInterestingTweets (reason,
                limit));
        return "index";
    }

    /**
     * Get all tweets belonging to a conversation
     *
     * @param convId the conversation id of the conversation
     */
    @GetMapping("conversation/{convId}")
    public String getConversation(@PathVariable String convId, Model model){
        model.addAttribute ("tweets",tweetRepository.findTweetsByConversationId (convId));
        return "index";
    }

    /**
     * Get all the regex-ids registered for the pattern matching
     */
    @GetMapping("reasons")
    public String getReasons(Model model){
        model.addAttribute ("reasons",patternMatchingService.getInterestingReasonIds ());
        return "index";
    }

}

