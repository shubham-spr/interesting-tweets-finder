package com.listener.interestingtweetsfinder.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Regex {

    private String id;
    private String description;
    private String expression;
    private boolean caseSensitivity;

    public Regex(){}

    public Regex(String id, String expression, String description,boolean caseSensitivity){
        this.id=id;
        this.description=description;
        this.expression=expression;
        this.caseSensitivity=caseSensitivity;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getExpression() { return expression; }

    public void setExpression(String expression) { this.expression = expression; }

    public Boolean getCaseSensitivity() {
        return caseSensitivity;
    }

    public void setCaseSensitivity(Boolean caseSensitivity) {
        this.caseSensitivity = caseSensitivity;
    }

    @Override
    public String toString() {
        return "Regex{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", expression='" + expression + '\'' +
                ", caseSensitivity=" + caseSensitivity +
                '}';
    }
}

