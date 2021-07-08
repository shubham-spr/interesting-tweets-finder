package com.listener.interestingtweetsfinder.repository;

import com.listener.interestingtweetsfinder.model.Regex;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegexRepository extends MongoRepository<Regex, String> {

}
