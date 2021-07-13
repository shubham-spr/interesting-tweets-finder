package com.listener.interestingtweetsfinder.service;

import com.listener.interestingtweetsfinder.PatternUpdateScheduler;
import com.listener.interestingtweetsfinder.model.Regex;
import com.listener.interestingtweetsfinder.repository.RegexRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.regex.Pattern;

/**
 * A Service that will fetch the list of regex patterns from mongo repository
 * and match it against all the tweets consumed.
 *
 * To avoid fetching all the regex everytime for a match against each tweet, a concurrent hashmap
 * is used which is updated after every fixed interval by a {@link PatternUpdateScheduler}
 */
@Service
public class PatternMatchingService {

    private static final Logger logger = LoggerFactory.getLogger (PatternMatchingService.class);

    private static final int INITIAL_SCHEDULING_DELAY_SECS = 5;
    private static final int FIXED_SCHEDULING_DELAY_SECS = 5;

    /**
     * A scheduling executor service for updating patterns
     */
    private final ScheduledExecutorService scheduledExecutorService;

    private final RegexRepository regexRepository;

    /**
     * A map for storing all the compiled patterns
     */
    private final Map<String, Pattern> regexPatternMap;

    public PatternMatchingService(RegexRepository regexRepository){
        this.regexRepository=regexRepository;
        regexPatternMap=new ConcurrentHashMap<> ();
        for(Regex regex : regexRepository.findAll ()){
            if(regex.getCaseSensitive ()) {
                regexPatternMap.computeIfAbsent (
                        regex.getId (),
                        k -> Pattern.compile (regex.getExpression ())
                );
            }else {
                regexPatternMap.computeIfAbsent (
                        regex.getId (),
                        k -> Pattern.compile (regex.getExpression (), Pattern.CASE_INSENSITIVE)
                );
            }
        }
        scheduledExecutorService= Executors.newSingleThreadScheduledExecutor (
                r -> new Thread (r,"PatternUpdScheduler")
        );
        startUpdateScheduler ();
        logger.info ("Initially found "+regexPatternMap.size ()+" patterns! ");
    }

    private void startUpdateScheduler(){
        scheduledExecutorService.scheduleWithFixedDelay (new PatternUpdateScheduler (regexPatternMap,regexRepository),
                INITIAL_SCHEDULING_DELAY_SECS,
                FIXED_SCHEDULING_DELAY_SECS,
                TimeUnit.SECONDS
        );
    }

    /**
     * Match the given text against all the regex stored
     *
     * @param text to match
     * @return the list of regex-ids against which the text is a match
     */
    public List<String> findMatchingRegexIdsForText(String text){
        List<String> matches = new ArrayList<> ();
        for(Map.Entry<String,Pattern> entry: regexPatternMap.entrySet ()){
            if(entry.getValue ().matcher (text).matches ()){
                matches.add(entry.getKey ());
            }
        }
        return matches;
    }

    public boolean isInteresting(String text){
        for(Map.Entry<String,Pattern> entry: regexPatternMap.entrySet ()){
            if(entry.getValue ().matcher (text).matches ())
                return true;
        }
        return false;
    }

    public Set<String> getInterestingReasonIds(){
        return regexPatternMap.keySet ();
    }

    @PreDestroy
    public void stop(){
        logger.info ("Shutting down executor service");
        scheduledExecutorService.shutdown ();
        try {
            scheduledExecutorService.awaitTermination (2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.warn ("Not able to wait for termination. Forcefully shutting down service!");
            e.printStackTrace ();
        }
        scheduledExecutorService.shutdownNow ();
    }
}
