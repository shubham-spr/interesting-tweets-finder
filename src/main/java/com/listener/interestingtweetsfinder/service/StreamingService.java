package com.listener.interestingtweetsfinder.service;

import com.listener.interestingtweetsfinder.TweetStreamerRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class StreamingService {

    private static final Logger logger= LoggerFactory.getLogger (StreamingService.class);

    private final ExecutorService executor;

    public StreamingService(KafkaTweetProducer producer){
        executor = Executors.newSingleThreadExecutor ( r -> new Thread (r,"TweetStreamingThread"));
        try {
            executor.execute (new TweetStreamerRunnable (producer));
        }catch (URISyntaxException e){
            logger.error ("Not able to run Tweet Streamer");
        }
    }

    @PreDestroy
    public void stop(){
        logger.info ("Shutting down executor service");
        try {
            executor.awaitTermination (2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.warn ("Not able to wait for termination. Forcefully shutting down service!");
            e.printStackTrace ();
        }
        executor.shutdownNow ();
    }


}
