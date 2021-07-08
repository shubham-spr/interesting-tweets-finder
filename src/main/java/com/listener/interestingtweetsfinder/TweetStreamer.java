package com.listener.interestingtweetsfinder;

import com.listener.interestingtweetsfinder.system.CredentialManager;
import com.listener.interestingtweetsfinder.system.Credentials;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class TweetStreamer implements Runnable {

    private static final String BASE_URL = "https://api.twitter.com/2/tweets/sample/stream";

    private final HttpClient httpClient;
    private final HttpGet httpGet;

    public TweetStreamer() throws URISyntaxException {
        httpClient= HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        CredentialManager credentialManager =CredentialManager.getInstance ();
        URIBuilder uriBuilder = new URIBuilder (BASE_URL);
        httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization",
                String.format("Bearer %s",credentialManager.get (Credentials.TWITTER_BEARER_TOKEN)));
    }

    public void run(){
        try {
            HttpResponse response = httpClient.execute (httpGet);
            HttpEntity entity = response.getEntity ();
            if (null != entity) {
                BufferedReader reader = new BufferedReader (new InputStreamReader ((entity.getContent ())));
                String line = reader.readLine ();
                while (line != null) {
                    System.out.println (line);
                    line = reader.readLine ();
                }
            }
        }catch (IOException e){
            e.printStackTrace ();
        }
    }

}
