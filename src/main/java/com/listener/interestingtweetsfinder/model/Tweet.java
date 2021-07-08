package com.listener.interestingtweetsfinder.model;

import ch.qos.logback.core.joran.spi.DefaultClass;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
public class Tweet {

    private String id;
    private String conversation_id;
    private List<ReferencedTweet> referenced_tweets;
    private String text;

    public Tweet(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public List<ReferencedTweet> getReferenced_tweets() {
        return referenced_tweets;
    }

    public void setReferenced_tweets(List<ReferencedTweet> referenced_tweets) {
        this.referenced_tweets = referenced_tweets;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String,String> toMap(){
        Map<String,String> newMap = new HashMap<> ();
        newMap.put ("id",id);
        newMap.put ("conversation_id",conversation_id);
        newMap.put ("text",text);
        return newMap;
    }

}
