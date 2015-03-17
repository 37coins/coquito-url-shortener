package com._37coins.coquito.resources;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com._37coins.coquito.pojo.ShortenerRequest;
import com._37coins.coquito.pojo.ShortenerResponse;
import com.hazelcast.core.IMap;


@Path(ShortenerResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class ShortenerResource {
    public static Logger log = LoggerFactory.getLogger(ShortenerResource.class);
    public final static String PATH = "/request";

	private final IMap<String,String> cache;
	private HttpServletRequest request;
	
	@Inject
	public ShortenerResource(IMap<String,String> cache, @Context HttpServletRequest request){
		this.cache = cache;
		this.request = request;
	}
	
	@POST
	public ShortenerResponse create(ShortenerRequest request){
	    if (cache.containsValue(request.getLongUrl())){
	        throw new WebApplicationException(Response.Status.CONFLICT);
	    }
	    String key = RandomStringUtils.random(4, "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ123456789");
	    cache.put(key, request.getLongUrl());
	    return new ShortenerResponse().setShortUrl(this.request.getHeader("Origin")+"/"+key);
	}

}
