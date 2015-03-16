package com._37coins.coquito.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ShortenerRequest implements Serializable{
    private static final long serialVersionUID = -3093359313004211533L;
    
    private String longUrl;

    public String getLongUrl() {
        return longUrl;
    }

    public ShortenerRequest setLongUrl(String longUrl) {
        this.longUrl = longUrl;
        return this;
    }

}
