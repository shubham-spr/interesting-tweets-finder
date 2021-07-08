package com.listener.interestingtweetsfinder.repository;

import com.listener.interestingtweetsfinder.model.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TweetRepository extends MongoRepository<Tweet,String> {

}
