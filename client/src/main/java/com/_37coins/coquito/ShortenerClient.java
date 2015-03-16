package com._37coins.coquito;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com._37coins.coquito.pojo.ShortenerRequest;
import com._37coins.coquito.pojo.ShortenerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShortenerClient {
    public static final String PHONE_REGEX = "^(\\+|\\d)[0-9]{7,16}$";
    private static final String REQUEST_PATH = "/request";
    private static final Logger log = LoggerFactory.getLogger(ShortenerClient.class);

    private HttpClient httpClient;
    private String uri;

    public ShortenerClient(String uri){
        this(uri, HttpClientFactory.getClientBuilder().build());
    }

    public ShortenerClient(String uri, HttpClient httpClient) {
        this.uri = uri;
        this.httpClient = httpClient;
    }

    public ShortenerResponse request(String longUrl) throws ShortenerClientException {
        HttpPost req = new HttpPost(uri + REQUEST_PATH);
        try {
            String reqValue = new ObjectMapper().writeValueAsString(new ShortenerRequest().setLongUrl(longUrl));
            StringEntity entity = new StringEntity(reqValue, "UTF-8");
            entity.setContentType("application/json");
            req.setEntity(entity);
            HttpResponse response = httpClient.execute(req);
            return new ObjectMapper().readValue(response.getEntity().getContent(), ShortenerResponse.class);
        } catch (IOException e){
            log.error("products client error", e);
            throw new ShortenerClientException(ShortenerClientException.Reason.ERROR_PARSING, e);
        }
    }
}
