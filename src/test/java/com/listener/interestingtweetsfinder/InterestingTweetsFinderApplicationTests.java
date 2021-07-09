package com.listener.interestingtweetsfinder;

import com.listener.interestingtweetsfinder.model.Regex;
import com.listener.interestingtweetsfinder.repository.RegexRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
class InterestingTweetsFinderApplicationTests {

	@Autowired
	private RegexRepository regexRepository;

	@Test
	public void addRegex(){
		regexRepository.save(new Regex (
				"regex-chinese",
				".*[\\p{IsHan}]+.*",
				"Contains Chinese/Japanese Text",
				false)
		);
		regexRepository.save(new Regex (
				"regex-donald",
				".*(donald|trump|potus|realDonaldTrump).*",
				"Contains mention of donald trump",
				false)
		);
		regexRepository.save(new Regex (
				"regex-sprinklr",
				".*(#sprinklr|@Sprinklr|#sprinklrlife).*",
				"A Tweet for Sprinklr",
				false)
		);
		regexRepository.save(new Regex (
				"regex-referenced",
				"RT .*",
				"A Retweeted Tweet",
				false
		));
	}

}
