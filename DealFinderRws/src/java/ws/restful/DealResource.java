/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.DealSessionBeanLocal;
import entity.Deal;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import util.exception.DealNotFoundException;

/**
 *
 * @author stellaang
 */
public class DealResource {
    
    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;

    private final DealSessionBeanLocal dealSessionBeanLocal;

    public DealResource() {
        
        sessionBeanLookup = new SessionBeanLookup();
        
        dealSessionBeanLocal = sessionBeanLookup.lookupDealSessionBeanLocal();
        
    }
    
    @Path("retrieveAllDeals")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllDeals()
    {
        try
        {
            List<Deal> deal = dealSessionBeanLocal.retrieveAllDeals();
            
            GenericEntity<List<Deal>> genericEntity = new GenericEntity<List<Deal>>(deal) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveDealByDealSerialNum/{serialNum}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveDealByDealSerialNum(@PathParam("serialNum") String serialNum)
    {
        try
        {

            Deal deal = dealSessionBeanLocal.retrieveDealByDealSerialNum(serialNum);
            
            deal.getBusiness();
            deal.getCategory();
            deal.getCustomers().clear();
            deal.getFavCustomers().clear();
            deal.getReviews().clear();
            deal.getSaleTransactions().clear();
            deal.getTags().clear();
            
            return Response.status(Status.OK).entity(deal).build();
        }
        catch(DealNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveDealByDealId/{dealId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveDealByDealId(@PathParam("dealId") long dealId)
    {
        try
        {

            Deal deal = dealSessionBeanLocal.retrieveDealByDealId(dealId);
            
            deal.getBusiness();
            deal.getCategory();
            deal.getCustomers().clear();
            deal.getFavCustomers().clear();
            deal.getReviews().clear();
            deal.getSaleTransactions().clear();
            deal.getTags().clear();
            
            return Response.status(Status.OK).entity(deal).build();
        }
        catch(DealNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    
}
