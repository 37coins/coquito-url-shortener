package com._37coins.coquito.resources;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hazelcast.core.IMap;


@Path(IndexResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class IndexResource {
	public final static String PATH = "/";
	
    private final IMap<String,String> cache;
    
    @Inject
    public IndexResource(IMap<String,String> cache){
        this.cache = cache;
    }

	@GET
	@Path("{key}")
	public Response getIndex(@PathParam("key") String key) throws URISyntaxException{
	    String longUrl = cache.get(key);
	    if (null!=longUrl){
	        return Response.seeOther(new URI(longUrl)).build();
	    }else{
	        throw new WebApplicationException(Response.Status.NOT_FOUND);
	    }
	}
	
}
