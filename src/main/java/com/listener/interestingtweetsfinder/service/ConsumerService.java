package com.listener.interestingtweetsfinder.service;

import com.listener.interestingtweetsfinder.model.Regex;
import com.listener.interestingtweetsfinder.repository.RegexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class ConsumerService {

    private static final Logger logger = LoggerFactory.getLogger (ConsumerService.class);

    private final RegexRepository regexRepository;
    private final Map<String, Pattern> regexPatternMap;

    public ConsumerService(RegexRepository regexRepository){
        this.regexRepository=regexRepository;
        regexPatternMap=new ConcurrentHashMap<> ();
        List<Regex> allRegex = regexRepository.findAll ();
        for(Regex regex: allRegex){
            addCompiledRegex(regex);
        }
    }

    private void addCompiledRegex(Regex regex){
        Pattern pattern= Pattern.compile (regex.getExpression ());
        regexPatternMap.putIfAbsent (regex.getId (),pattern);
    }

}
