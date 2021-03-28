/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.BusinessSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Business;
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
import util.exception.BusinessNotFoundException;

/**
 *
 * @author stellaang
 */
@Path("Business")
public class BusinessResource {
    
    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;

    private final BusinessSessionBeanLocal businessSessionBeanLocal;

    public BusinessResource() {
        
        sessionBeanLookup = new SessionBeanLookup();
        
        businessSessionBeanLocal = sessionBeanLookup.lookupBusinessSessionBeanLocal();        
    }
    
    
    @Path("retrieveAllBusiness")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllBusiness() {
        try {
            List<Business> business = businessSessionBeanLocal.getAllBusinesses();

            GenericEntity<List<Business>> genericEntity = new GenericEntity<List<Business>>(business) {
            };

            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveBusinessByBusinessId/{businessId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveBusinessById(@PathParam("businessId") long businessId)
    {
        try
        {

            Business business = businessSessionBeanLocal.getBusinessByBusinessId(businessId);
            
            business.setPassword(null);
            business.setSalt(null);            
            business.getSaleTransactions().clear();
            
            return Response.status(Status.OK).entity(business).build();
        }
        catch(BusinessNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveBusinessByUsername/{username}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveBusinessByUsername(@PathParam("username") String username)
    {
        try
        {

            Business business = businessSessionBeanLocal.getBusinessByUsername(username);
            
            business.setPassword(null);
            business.setSalt(null);            
            business.getSaleTransactions().clear();
            
            return Response.status(Status.OK).entity(business).build();
        }
        catch(BusinessNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
}
