package com.listener.interestingtweetsfinder.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class StreamElement {

    private static final ObjectMapper mapper= new ObjectMapper ();
    private static final Logger logger = LoggerFactory.getLogger (StreamElement.class);

    private Tweet data;

    public StreamElement(){}

    public Tweet getData() {
        return data;
    }

    public void setData(Tweet data) {
        this.data = data;
    }
    
    public static Optional<StreamElement> fromString(String elementString){
        Optional<StreamElement> streamElementOptional=Optional.empty ();
        try {
            streamElementOptional = Optional.of(mapper.readValue (elementString,StreamElement.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace ();
            logger.error ("Cannot map "+elementString+" to Stream Element");
        }
        return streamElementOptional;
    }

    @Override
    public String toString() {
        return "StreamElement{" +
                "data=" + data +
                '}';
    }
}
