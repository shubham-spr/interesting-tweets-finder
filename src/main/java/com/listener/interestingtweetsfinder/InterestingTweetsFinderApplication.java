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

	@Bean
	public CommandLineRunner commandLineRunner(RegexRepository regexRepository) {
		return (args) -> {
		};
	}

}
