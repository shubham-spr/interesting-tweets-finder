package com.listener.interestingtweetsfinder.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CredentialManager {

    private static final Logger logger = LoggerFactory.getLogger (CredentialManager.class);
    private static final CredentialManager INSTANCE = new CredentialManager ();
    private static final String CREDENTIALS_FILE_PATH = "/Users/shubham/Projects/interesting-tweets-finder/src/main" +
            "/resources/credentials" +
            ".properties";

    private final Properties properties;

    private CredentialManager(){
        properties= new Properties();
        try{
            properties.load(new BufferedInputStream (new FileInputStream (CREDENTIALS_FILE_PATH)));
        } catch (IOException e) {
            logger.info ("Not able to load the credentials file: "+ CREDENTIALS_FILE_PATH);
        }
        for(Credentials credentials: Credentials.values ()){
            if(get(credentials)==null) {
                logger.info ("Not able to get property "+ credentials +" from "+CREDENTIALS_FILE_PATH);
            }
        }
    }

    public static CredentialManager getInstance(){
        return INSTANCE;
    }

    public String get(Credentials credentials){
        return properties.getProperty (credentials.toString ());
    }

}
