package com._37coins.coquito.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
	@Path("*")
	public Response getIndex(){
	    return Response.seeOther(null).build();
	}
	
}
