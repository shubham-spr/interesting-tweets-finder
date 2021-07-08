package com.listener.interestingtweetsfinder.repository;

import com.listener.interestingtweetsfinder.model.Tweet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TweetRepository extends ElasticsearchRepository<Tweet,String> {

    List<Tweet> findTweetsByConversationId(String conversationId);

    Tweet findTweetById(String id);

}
