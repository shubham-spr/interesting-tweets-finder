package com.listener.interestingtweetsfinder.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Regex {

    private String id;
    private String description;
    private String expression;

    public Regex(){}

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getExpression() { return expression; }

    public void setExpression(String expression) { this.expression = expression; }
}
