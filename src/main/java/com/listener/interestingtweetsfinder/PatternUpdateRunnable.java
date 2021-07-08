package com.listener.interestingtweetsfinder;

import com.listener.interestingtweetsfinder.model.Regex;
import com.listener.interestingtweetsfinder.repository.RegexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class PatternUpdateRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger (PatternUpdateRunnable.class);

    private final Map<String, Pattern> regexPatternMap;
    private final RegexRepository regexRepository;

    public PatternUpdateRunnable(Map<String,Pattern> regexPatternMap, RegexRepository regexRepository) {
        this.regexPatternMap=regexPatternMap;
        this.regexRepository=regexRepository;
    }

    @Override
    public void run() {
        List<Regex> list= regexRepository.findAll();
        Set<String> present = new HashSet<> ();
        // Add new ids
        for(final Regex regex:list) {
            regexPatternMap.computeIfAbsent (regex.getId (),(key)-> Pattern.compile (regex.getExpression ()));
            present.add (regex.getId ());
        }
        // Remove the deleted ids
        Set<String> regexIds = regexPatternMap.keySet ();
        for(String regexId: regexIds){
            if(!present.contains (regexId)){
                regexPatternMap.remove (regexId);
            }
        }
        logger.info ("Patterns Updated! Current Patterns: "+regexPatternMap.size ());
    }

}
