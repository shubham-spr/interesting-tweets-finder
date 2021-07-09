package com.listener.interestingtweetsfinder;

import com.listener.interestingtweetsfinder.model.Regex;
import com.listener.interestingtweetsfinder.repository.RegexRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class RegexRepositoryTest {

    @MockBean
    private RegexRepository regexRepository;

    private Regex regex;

    @BeforeEach
    public void addSampleRegex(){
        regex = new Regex (
                "regex-chinese",
                ".*[\\p{IsHan}]+.*",
                "Contains Chinese/Japanese Text",
                false);
        regexRepository.save(regex);
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

    @Test
    void checkRegexExists(){
        Optional<Regex> res = regexRepository.findById ("regex-chinese");
        assert (res.isPresent ());
        assert (res.get ().equals (regex));
    }
}
