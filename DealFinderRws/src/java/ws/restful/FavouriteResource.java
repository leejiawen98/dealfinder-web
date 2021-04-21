/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.FavouriteSessionBeanLocal;
import entity.Favourite;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import util.exception.FavouriteNotFoundException;
import ws.datamodel.CreateFavReq;

/**
 *
 * @author stellaang
 */
@Path("Favourite")
public class FavouriteResource {
    
    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;
    
    private final FavouriteSessionBeanLocal favouriteSessionBeanLocal;  
    
    public FavouriteResource() {
        
        sessionBeanLookup = new SessionBeanLookup();
        
        favouriteSessionBeanLocal = sessionBeanLookup.lookupFavouriteSessionBeanLocal();
        
    }
    
    @Path("retrieveAllFavourites")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllFavourites()
    {
        try
        {
            List<Favourite> favourites = favouriteSessionBeanLocal.retrieveAllFavourites();
            
            for(Favourite f : favourites) {
                if(f.getCustomer() != null) {
                    f.getCustomer().getRedemptions().clear();
                    f.getCustomer().getFavourites().clear();
                    f.getCustomer().getReviews().clear();
                    f.getCustomer().getDeals().clear();
                    f.getCustomer().getSaleTransactions().clear();
                    f.getCustomer().setCreditCard(null);
                   
                }
                if(f.getDeal() != null) {
                    f.getDeal().getSaleTransactions().clear();
                if(f.getDeal().getBusiness() != null) {
                    f.getDeal().getBusiness().getSaleTransactions().clear();
                    f.getDeal().getBusiness().getDeals().clear();
                }
                if(f.getDeal().getCategory() != null) {
                    f.getDeal().getCategory().getDeals().clear();
                    f.getDeal().getCategory().getSubCategories().clear();
                    if (f.getDeal().getCategory().getParentCategory() != null) {
                        f.getDeal().getCategory().getParentCategory().getSubCategories().clear();
                    }
                }
                f.getDeal().getRedemptions().clear();
                f.getDeal().getCustomers().clear();
                 f.getDeal().getFavourites().clear();
                f.getDeal().getReviews().clear();
                f.getDeal().getTags().clear();
                }
                
            }
            
            GenericEntity<List<Favourite>> genericEntity = new GenericEntity<List<Favourite>>(favourites) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewFavourite(CreateFavReq createFavReq) {
        if (createFavReq != null) {
            try { 
                Favourite favourite = favouriteSessionBeanLocal.createNewFavourite(createFavReq.getCustomerId(), createFavReq.getDealId(), createFavReq.getFavourite());

                return Response.status(Response.Status.OK).entity(favourite).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }
    
    @Path("retrieveFavouriteByCustomerId/{customerId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveFavouriteByCustomerId(@PathParam("customerId") long customerId)
    {
        try
        {

            List<Favourite> favourites = favouriteSessionBeanLocal.retrieveFavByCustomerId(customerId);
            
            for(Favourite f : favourites) {
                if(f.getCustomer() != null) {
                    f.getCustomer().getRedemptions().clear();
                    f.getCustomer().getFavourites().clear();
                    f.getCustomer().getReviews().clear();
                    f.getCustomer().getDeals().clear();
                    f.getCustomer().getSaleTransactions().clear();
                    
                    f.getCustomer().getCreditCard().setCustomer(null);
                }
                if(f.getDeal() != null) {
                    f.getDeal().getSaleTransactions().clear();
                if(f.getDeal().getBusiness() != null) {
                    f.getDeal().getBusiness().getSaleTransactions().clear();
                    f.getDeal().getBusiness().getDeals().clear();
                }
                if(f.getDeal().getCategory() != null) {
                    f.getDeal().getCategory().getDeals().clear();
                    f.getDeal().getCategory().getSubCategories().clear();
                    if (f.getDeal().getCategory().getParentCategory() != null) {
                        f.getDeal().getCategory().getParentCategory().getSubCategories().clear();
                    }
                }
                f.getDeal().getRedemptions().clear();
                f.getDeal().getCustomers().clear();
                 f.getDeal().getFavourites().clear();
                f.getDeal().getReviews().clear();
                f.getDeal().getTags().clear();
                }
                
            }
            
            return Response.status(Status.OK).entity(favourites).build();
        }

        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("{favouriteId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFavourite(@PathParam("favouriteId") Long favouriteId)
    {
        try
        {
            favouriteSessionBeanLocal.deleteFavourite(favouriteId);

            return Response.status(Status.OK).build();
        }
        catch(FavouriteNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
}
