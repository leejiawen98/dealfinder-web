/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.RedemptionSessionBeanLocal;
import entity.Redemption;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import util.exception.RedemptionNotFoundException;
import ws.datamodel.CreateRedemptionReq;

/**
 *
 * @author stellaang
 */
@Path("Redemption")
public class RedemptionResource {
    
    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;
    
    private final RedemptionSessionBeanLocal redemptionSessionBeanLocal;
    
    public RedemptionResource() {
        
        sessionBeanLookup = new SessionBeanLookup();
        
        redemptionSessionBeanLocal = sessionBeanLookup.lookupRedemptionSessionBeanLocal();
        
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewRedemption(CreateRedemptionReq createRedemption) {
        System.err.println(createRedemption.getCustomerId());
        if (createRedemption != null) {
            try { 
                Redemption redemption = redemptionSessionBeanLocal.createNewRedemption(createRedemption.getCustomerId(), createRedemption.getRedemption(), createRedemption.getDealId());
                return Response.status(Response.Status.OK).entity(redemption.getRedemptionId()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }
    
    @Path("retrieveAllRedemptions")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllRedemptions()
    {
        try
        {
            List<Redemption> redemptions = redemptionSessionBeanLocal.retrieveAllRedemptions();
            
            for(Redemption r: redemptions) {
                if(r.getDeal() != null) {
                    r.getDeal().getSaleTransactions().clear();
                    if (r.getDeal().getBusiness() != null) {
                        r.getDeal().getBusiness().getSaleTransactions().clear();
                        r.getDeal().getBusiness().getDeals().clear();
                        
                    }
                    if (r.getDeal().getCategory() != null) {
                        r.getDeal().getCategory().getDeals().clear();
                        r.getDeal().getCategory().getSubCategories().clear();
                        if (r.getDeal().getCategory().getParentCategory() != null) {
                            r.getDeal().getCategory().getParentCategory().getSubCategories().clear();
                        }
                    }
                    r.getDeal().getCustomers().clear();
                    r.getDeal().getFavourites().clear();
                    r.getDeal().getReviews().clear();
                    r.getDeal().getTags().clear();
                    r.getDeal().getRedemptions().clear();
                }
                
                if(r.getCustomer() != null) {
                    r.getCustomer().setPassword(null);
                    r.getCustomer().setSalt(null);
                    r.getCustomer().getSaleTransactions().clear();
                    r.getCustomer().getRedemptions().clear();
                    r.getCustomer().getFavourites().clear();
                    r.getCustomer().getReviews().clear();
                    r.getCustomer().getDeals().clear();
                    r.getCustomer().getCreditCard().setCustomer(null);
                }
                
            }
            
            GenericEntity<List<Redemption>> genericEntity = new GenericEntity<List<Redemption>>(redemptions) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveRedemptionByCustomerId/{customerId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveRedemptionByCustomerId(@PathParam("customerId") long customerId)
    {
        try
        {

            List<Redemption> redemption = redemptionSessionBeanLocal.retrieveRedemptionByCustomerId(customerId);
            
            for(Redemption r: redemption) {
                if(r.getDeal() != null) {
                    r.getDeal().getSaleTransactions().clear();
                    if (r.getDeal().getBusiness() != null) {
                        r.getDeal().getBusiness().getSaleTransactions().clear();
                        r.getDeal().getBusiness().getDeals().clear();
                    }
                    if (r.getDeal().getCategory() != null) {
                        r.getDeal().getCategory().getDeals().clear();
                        r.getDeal().getCategory().getSubCategories().clear();
                        if (r.getDeal().getCategory().getParentCategory() != null) {
                            r.getDeal().getCategory().getParentCategory().getSubCategories().clear();
                        }
                    }
                    r.getDeal().getCustomers().clear();
                    r.getDeal().getFavourites().clear();
                    r.getDeal().getReviews().clear();
                    r.getDeal().getTags().clear();
                    r.getDeal().getRedemptions().clear();
                    r.getDeal().getReviews().clear();
                    
                }
                
                if(r.getCustomer() != null) {
                    r.getCustomer().setPassword(null);
                    r.getCustomer().setSalt(null);
                    r.getCustomer().getSaleTransactions().clear();
                    r.getCustomer().getDeals().clear();
                    r.getCustomer().getFavourites().clear();
                    r.getCustomer().getReviews().clear();
                    r.getCustomer().getRedemptions().clear();
                    r.getCustomer().getCreditCard().setCustomer(null);
                }
                
            }
            
            GenericEntity<List<Redemption>> genericEntity = new GenericEntity<List<Redemption>>(redemption) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRedemption(Redemption r) {
        if (r != null) {
            try { 
                Redemption redemption = redemptionSessionBeanLocal.updateRedemption(r);
                return Response.status(Response.Status.OK).entity(redemption.getRedemptionId()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update request").build();
        }
    }
    
    @Path("retrieveRedemptionsByCustIdandBizId/{customerId}/{bizId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveRedemptionsByCustIdandBizId(@PathParam("customerId") long customerId, @PathParam("bizId") long bizId)
    {
        try
        {
            List<Redemption> redemption = redemptionSessionBeanLocal.retrieveRedemptionsByCustIdandBizId(customerId, bizId);
            
            for(Redemption r: redemption) {
                if(r.getDeal() != null) {
                    r.getDeal().getSaleTransactions().clear();
                    if (r.getDeal().getBusiness() != null) {
                        r.getDeal().getBusiness().getSaleTransactions().clear();
                        r.getDeal().getBusiness().getDeals().clear();
                    }
                    if (r.getDeal().getCategory() != null) {
                        r.getDeal().getCategory().getDeals().clear();
                        r.getDeal().getCategory().getSubCategories().clear();
                        if (r.getDeal().getCategory().getParentCategory() != null) {
                            r.getDeal().getCategory().getParentCategory().getSubCategories().clear();
                        }
                    }
                    r.getDeal().getCustomers().clear();
                    r.getDeal().getFavourites().clear();
                    r.getDeal().getReviews().clear();
                    r.getDeal().getTags().clear();
                    r.getDeal().getRedemptions().clear();
                    r.getDeal().getReviews().clear();
                    
                }
                
                if(r.getCustomer() != null) {
                    r.getCustomer().setPassword(null);
                    r.getCustomer().setSalt(null);
                    r.getCustomer().getSaleTransactions().clear();
                    r.getCustomer().getDeals().clear();
                    r.getCustomer().getFavourites().clear();
                    r.getCustomer().getReviews().clear();
                    r.getCustomer().getRedemptions().clear();
                    r.getCustomer().setCreditCard(null);
                }
                
            }
            
            GenericEntity<List<Redemption>> genericEntity = new GenericEntity<List<Redemption>>(redemption) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
