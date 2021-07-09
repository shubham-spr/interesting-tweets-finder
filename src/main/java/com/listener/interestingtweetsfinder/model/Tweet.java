package com.listener.interestingtweetsfinder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document (indexName = "tweets")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {

    @Id
    private String id;

    @JsonProperty("conversation_id")
    private String conversationId;

    @JsonProperty("referenced_tweets")
    private List<ReferencedTweet> referencedTweets;

    private String text;

    public Tweet(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public List<ReferencedTweet> getReferencedTweets() {
        return referencedTweets;
    }

    public void setReferencedTweets(List<ReferencedTweet> referencedTweets) {
        this.referencedTweets = referencedTweets;
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
        newMap.put ("conversationId", conversationId);
        newMap.put ("text",text);
        return newMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        Tweet tweet = (Tweet) o;
        return Objects.equals (id, tweet.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode () : 0;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id='" + id + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", referencedTweets=" + referencedTweets +
                ", text='" + text + '\'' +
                '}';
    }
}
