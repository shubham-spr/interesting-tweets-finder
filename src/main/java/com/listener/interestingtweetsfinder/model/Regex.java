package com.listener.interestingtweetsfinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class Regex {

    @Id
    private String id;

    private String description;
    private String expression;
    private boolean caseSensitive;

    public Regex(){}

    public Regex(String id, String expression, String description,boolean caseSensitive){
        this.id=id;
        this.description=description;
        this.expression=expression;
        this.caseSensitive = caseSensitive;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getExpression() { return expression; }

    public void setExpression(String expression) { this.expression = expression; }

    public Boolean getCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(Boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public String toString() {
        return "Regex{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", expression='" + expression + '\'' +
                ", caseSensitive=" + caseSensitive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        Regex regex = (Regex) o;
        return Objects.equals (id, regex.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode () : 0;
    }
}

