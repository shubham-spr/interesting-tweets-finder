package com.listener.interestingtweetsfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.listener.interestingtweetsfinder")
public class InterestingTweetsFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterestingTweetsFinderApplication.class, args);
	}

}
