package com._37coins.coquito.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ShortenerResponse implements Serializable{
    private static final long serialVersionUID = 3824285196882694823L;
    
    private String shortUrl;

    public String getShortUrl() {
        return shortUrl;
    }

    public ShortenerResponse setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
        return this;
    }

}
