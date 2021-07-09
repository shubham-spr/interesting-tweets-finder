package com.listener.interestingtweetsfinder.model;

public class ReferencedTweet {

    private ReferenceType type;
    private String id;

    public ReferencedTweet(){}

    public ReferenceType getType() { return type; }

    public void setType(ReferenceType type) { this.type = type; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    @Override
    public String toString() {
        return "ReferencedTweet{" +
                "type=" + type +
                ", id='" + id + '\'' +
                '}';
    }
}

