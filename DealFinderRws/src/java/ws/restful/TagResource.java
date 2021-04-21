/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.TagSessionBeanLocal;
import entity.Tag;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import util.exception.TagNotFoundException;

/**
 *
 * @author stellaang
 */
@Path("Tag")
public class TagResource {
    
    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;

    private final TagSessionBeanLocal tagSessionBeanLocal;

    public TagResource() {
        
        sessionBeanLookup = new SessionBeanLookup();
        
        tagSessionBeanLocal = sessionBeanLookup.lookupTagSessionBeanLocal();
        
    }
    
    @Path("retrieveAllTags")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTag()
    {
        try
        {
            List<Tag> tag = tagSessionBeanLocal.retrieveAllTags();
            
            for(Tag t: tag) {
                t.getDeals().clear();
            }
            GenericEntity<List<Tag>> genericEntity = new GenericEntity<List<Tag>>(tag) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveTagByTagId/{tagId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveTagByTagId(@PathParam("tagId") long tagId)
    {
        try
        {

            Tag tag = tagSessionBeanLocal.retrieveTagByTagId(tagId);
            
            tag.getDeals().clear();

            return Response.status(Status.OK).entity(tag).build();
        }
        catch(TagNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
}
