package com.listener.interestingtweetsfinder;

import com.listener.interestingtweetsfinder.service.KafkaTweetProducer;
import com.listener.interestingtweetsfinder.utils.CredentialManager;
import com.listener.interestingtweetsfinder.utils.Credentials;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;

import static java.lang.Math.max;
import static org.apache.kafka.common.utils.Utils.formatAddress;
import static org.apache.kafka.common.utils.Utils.min;

public class TweetStreamerRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger (TweetStreamerRunnable.class);

    private static final String BASE_URL = "https://api.twitter.com/2/tweets/sample/stream";
    private static final String EXPANSIONS ="expansions=referenced_tweets.id";
    private static final String FIELDS ="tweet.fields=conversation_id,referenced_tweets";
    private static final long STATS_AFTER_NUM_TWEETS = 100;

    private static final long INITIAL_INTERVAL = 1000;
    private static final long MAX_INTERVAL = 20000;
    private static final int MULTIPLIER = 2;
    private static int counter;

    private long currentWaitInterval;
    private final KafkaTweetProducer producer;
    private final HttpClient httpClient;
    private final HttpGet httpGet;

    public TweetStreamerRunnable(KafkaTweetProducer kafkaTweetProducer) throws URISyntaxException {
        this.producer =kafkaTweetProducer;
        httpClient= HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        CredentialManager credentialManager =CredentialManager.getInstance ();
        URIBuilder uriBuilder = new URIBuilder (BASE_URL+"?"+FIELDS);
        httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization",
                String.format("Bearer %s",credentialManager.get (Credentials.TWITTER_BEARER_TOKEN)));
        currentWaitInterval =0;
    }

    private void resetBackoff(){
        currentWaitInterval = 0;
    }

    private void incrementBackoff(){
        currentWaitInterval = min(currentWaitInterval*MULTIPLIER,MAX_INTERVAL);
        currentWaitInterval = max(currentWaitInterval,INITIAL_INTERVAL);
    }

    public void run(){
        // sampleStream ();
        while (true){
            try {
                HttpResponse response = httpClient.execute (httpGet);
                HttpEntity entity = response.getEntity ();
                if (null != entity) {
                    BufferedReader reader = new BufferedReader (new InputStreamReader ((entity.getContent ())));
                    String line = reader.readLine ();
                    while (line != null) {
                        if (line.length()>0)
                          producer.sendMessage (line);
                        line = reader.readLine ();
                        counter++;
                        if(counter%STATS_AFTER_NUM_TWEETS==0){
                            logger.info ("Fetched and sent "+counter+" tweets in total to producer");
                        }
                        resetBackoff ();
                    }
                }
            }catch (IOException e){
                logger.error ("Disconnected from Twitter!");
                incrementBackoff ();
                e.printStackTrace ();
            }
            try {
                Thread.sleep (currentWaitInterval);
            } catch (InterruptedException e) {
                e.printStackTrace ();
                logger.error ("Not able to wait for backoff period!");
            }
        }
    }

    public void sampleStream(){
        try {
            BufferedReader reader = new BufferedReader (new FileReader ("src/main/resources/sampleStream.stream"));
            String line = reader.readLine ();
            while (line != null) {
                if(line.length ()>0)
                    producer.sendMessage (line);
                line = reader.readLine ();
            }
        }catch (IOException e){
            logger.error ("Cannot read sample Stream!");
            e.printStackTrace ();
        }
    }


}
