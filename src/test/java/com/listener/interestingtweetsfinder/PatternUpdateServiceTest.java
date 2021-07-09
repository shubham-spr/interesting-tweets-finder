package com.listener.interestingtweetsfinder;

import com.listener.interestingtweetsfinder.model.Regex;
import com.listener.interestingtweetsfinder.repository.RegexRepository;
import com.listener.interestingtweetsfinder.service.PatternMatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class PatternUpdateServiceTest {

    @Autowired
    private PatternMatchingService patternMatchingService;

    @Autowired
    private RegexRepository regexRepository;

    @BeforeEach
    public void setUp(){
        Regex regex = new Regex (
            "regex-chinese",
            ".*[\\p{IsHan}]+.*",
            "Contains Chinese/Japanese Text",
            false);
        regexRepository.deleteById (regex.getId ());
        regexRepository.save (regex);
    }

    @Test
    public void checkForPatternUpdate() throws InterruptedException {
        Thread.sleep (6000);
        Set<String> regexIds = patternMatchingService.getInterestingReasonIds ();
        assert (regexIds.contains ("regex-chinese"));
    }

}
