/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.DealSessionBeanLocal;
import entity.Customer;
import entity.Deal;
import entity.Review;
import entity.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import util.exception.CategoryNotFoundException;
import util.exception.DealNotFoundException;

/**
 *
 * @author stellaang
 */
@Path("Deal")
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
            
            for(Deal d: deal) {
                
                d.getSaleTransactions().clear();
                if(d.getBusiness() != null) {
                    d.getBusiness().getSaleTransactions().clear();
                    d.getBusiness().getDeals().clear();
                }
                if(d.getCategory() != null) {
                    d.getCategory().getDeals().clear();
                    d.getCategory().getSubCategories().clear();
                    if (d.getCategory().getParentCategory() != null) {
                        d.getCategory().getParentCategory().getSubCategories().clear();
                    }
                }
                d.getRedemptions().clear();
                d.getCustomers().clear();
                 d.getFavourites().clear();
                d.getReviews().clear();
                d.getTags().clear();
            }
            
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

            Deal d = dealSessionBeanLocal.retrieveDealByDealSerialNum(serialNum);
            
            d.getSaleTransactions().clear();
            if (d.getBusiness() != null) {
                d.getBusiness().getSaleTransactions().clear();
                d.getBusiness().getDeals().clear();
            }
            if (d.getCategory() != null) {
                d.getCategory().getDeals().clear();
                d.getCategory().getSubCategories().clear();
                if (d.getCategory().getParentCategory() != null) {
                    d.getCategory().getParentCategory().getSubCategories().clear();
                }
            }
            d.getRedemptions().clear();
            d.getCustomers().clear();
            d.getFavourites().clear();
            d.getReviews().clear();
            d.getTags().clear();
            
            return Response.status(Status.OK).entity(d).build();
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

            Deal d = dealSessionBeanLocal.retrieveDealByDealId(dealId);
            
            d.getSaleTransactions().clear();
            if (d.getBusiness() != null) {
                d.getBusiness().getSaleTransactions().clear();
                d.getBusiness().getDeals().clear();
            }
            if (d.getCategory() != null) {
                d.getCategory().getDeals().clear();
                d.getCategory().getSubCategories().clear();
                if (d.getCategory().getParentCategory() != null) {
                    d.getCategory().getParentCategory().getSubCategories().clear();
                }
            }
            d.getRedemptions().clear();
            d.getCustomers().clear();
            d.getFavourites().clear();
            d.getReviews().clear();
            d.getTags().clear();
            
            return Response.status(Status.OK).entity(d).build();
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
    
    @Path("filterDealByTagId/{tagIds}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterDealByTagId(@PathParam("tagIds") String tagString)
    {
        try
        {
            String[] arr = tagString.split(",");
            List<Long> tagIds = new ArrayList<>();
            for(int x = 0; x < arr.length; x++) {
                tagIds.add(Long.parseLong(arr[x]));
            }
            List<Deal> deal = dealSessionBeanLocal.filterDealByTags(tagIds, "OR");
            
            for(Deal d: deal) {
                
                d.getSaleTransactions().clear();
                if(d.getBusiness() != null) {
                    d.getBusiness().getSaleTransactions().clear();
                    d.getBusiness().getDeals().clear();
                }
                if(d.getCategory() != null) {
                    d.getCategory().getDeals().clear();
                    d.getCategory().getSubCategories().clear();
                    if (d.getCategory().getParentCategory() != null) {
                        d.getCategory().getParentCategory().getSubCategories().clear();
                    }
                }
                d.getRedemptions().clear();
                d.getCustomers().clear();
                 d.getFavourites().clear();
                d.getReviews().clear();
                d.getTags().clear();
            }
            
            GenericEntity<List<Deal>> genericEntity = new GenericEntity<List<Deal>>(deal) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    
    @Path("retrievePurchasedDealsByCustIdandBizId/{custId}/{bizId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePurchasedDealsByCustIdandBizId(@PathParam("custId") long custId, @PathParam("bizId") long bizID)
    {
        try
        {

            List<Deal> deals = dealSessionBeanLocal.retrievePurchasedDealsByCustIdandBizId(custId, bizID);
            
            for(Deal d: deals) {
                
                
                if(d.getBusiness() != null) {
                    d.getBusiness().getSaleTransactions().clear();
                    d.getBusiness().getDeals().clear();
                }
                if(d.getCategory() != null) {
                    d.getCategory().getDeals().clear();
                    d.getCategory().getSubCategories().clear();
                    if (d.getCategory().getParentCategory() != null) {
                        d.getCategory().getParentCategory().getSubCategories().clear();
                    }
                }
                
                for (Customer c: d.getCustomers())
                {
                    c.getDeals().clear();
                    c.getSaleTransactions().clear();
                    c.getFavourites().clear();
                    c.getRedemptions().clear();
                    c.getReviews().clear();
                }
                
                d.getSaleTransactions().clear();
                d.getRedemptions().clear();
                d.getCustomers().clear();
                d.getFavourites().clear();
                d.getReviews().clear();
                d.getTags().clear();
            }
            
            GenericEntity<List<Deal>> genericEntity = new GenericEntity<List<Deal>>(deals) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
