package com.listener.interestingtweetsfinder;

import com.listener.interestingtweetsfinder.model.Regex;
import com.listener.interestingtweetsfinder.repository.RegexRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.listener.interestingtweetsfinder")
public class InterestingTweetsFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterestingTweetsFinderApplication.class, args);
	}

	public void addToRegex(RegexRepository regexRepository){
		regexRepository.save(new Regex ("regex-chinese",".*[\\p{IsHan}]+.*","Contains Chinese/Japanese Text",false));
		regexRepository.save(new Regex ("regex-donald",".*(donald|trump|potus|realDonaldTrump).*",
				"Contains mention of donald trump",false));
		regexRepository.save(new Regex ("regex-sprinklr",".*(#sprinklr|@Sprinklr|#sprinklrlife).*","A Tweet for " +
				"Sprinklr",
				false));
		regexRepository.save(new Regex ("regex-referenced","RT .*","A Retweeted Tweet",false));
	}

	@Bean
	public CommandLineRunner commandLineRunner(RegexRepository regexRepository){
		return (args)-> {
			// addToRegex (regexRepository);
		};
	}
}
