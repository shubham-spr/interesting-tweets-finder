package com.listener.interestingtweetsfinder;

import com.listener.interestingtweetsfinder.model.Regex;
import com.listener.interestingtweetsfinder.repository.RegexRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InterestingTweetsFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterestingTweetsFinderApplication.class, args);
	}

	public void addToRegex(RegexRepository regexRepository){
		regexRepository.save(new Regex ("regex-001",".*[\\p{IsHan}]+.*","Contains Chinese/Japanese Text",false));
		regexRepository.save(new Regex ("regex-002",".*(donald|trump|potus|realDonaldTrump).*",
				"Contains mention of donald trump",false));
		regexRepository.save(new Regex ("regex-003",".*(#sprinklr|@Sprinklr|#sprinklrlife).*","A Tweet for Sprinklr",
				false));
		regexRepository.save(new Regex ("regex-004","RT .*","A Retweeted Tweet",false));
	}

	@Bean
	public CommandLineRunner commandLineRunner(RegexRepository regexRepository){
		return (args)-> {
			// addToRegex (regexRepository);
		};
	}
}
