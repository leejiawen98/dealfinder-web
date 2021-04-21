/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.DealSessionBeanLocal;
import ejb.session.stateless.ReviewSessionBeanLocal;
import entity.Customer;
import entity.Deal;
import entity.Review;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import util.exception.CustomerNotFoundException;
import util.exception.DealNotFoundException;
import util.exception.ReviewNotFoundException;
import ws.datamodel.CreateNewReviewReq;

/**
 *
 * @author stellaang
 */
@Path("Review")
public class ReviewResource {
    
    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;

    private final ReviewSessionBeanLocal reviewSessionBeanLocal;

    public ReviewResource() {
        
        sessionBeanLookup = new SessionBeanLookup();
        
        reviewSessionBeanLocal = sessionBeanLookup.lookupReviewSessionBeanLocal();
        
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewReview(CreateNewReviewReq createNewReviewReq) {
        if (createNewReviewReq != null) {
            try { 
                Review review = reviewSessionBeanLocal.createNewReview(createNewReviewReq.getNewReview(), createNewReviewReq.getDealId(), createNewReviewReq.getCustomerId());

                return Response.status(Response.Status.OK).entity(review.getReviewId()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }
    
    @Path("retrieveAllReviews")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllReviews()
    {
        try
        {
            List<Review> review = reviewSessionBeanLocal.retrieveAllReviews();
            
            for(Review r: review) {
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
                    r.getDeal().getRedemptions().clear();
                    r.getDeal().getCustomers().clear();
                    r.getDeal().getFavourites().clear();
                    r.getDeal().getReviews().clear();
                    r.getDeal().getTags().clear();
                    
                    if(r.getCustomer() != null) {
                    r.getCustomer().getRedemptions().clear();
                    r.getCustomer().getFavourites().clear();
                    r.getCustomer().getReviews().clear();
                    r.getCustomer().getDeals().clear();
                    r.getCustomer().getSaleTransactions().clear();
                    r.getCustomer().getCreditCard().setCustomer(null);
                }
                }
            }
            
            GenericEntity<List<Review>> genericEntity = new GenericEntity<List<Review>>(review) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveReviewByDealId/{dealId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveReviewByDealId(@PathParam("dealId") long dealId)
    {
        try
        {

            List<Review> review = reviewSessionBeanLocal.retrieveReviewsByDealId(dealId);
            for(Review r: review) {
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
                    
                    if(r.getCustomer() != null) {
                    r.getCustomer().getRedemptions().clear();
                    r.getCustomer().getFavourites().clear();
                    r.getCustomer().getReviews().clear();
                    r.getCustomer().getDeals().clear();
                    r.getCustomer().getSaleTransactions().clear();
                }
                }
            }
            
            GenericEntity<List<Review>> genericEntity = new GenericEntity<List<Review>>(review) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
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
    
    @Path("retrieveReviewByReviewId/{reviewId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveReviewByReviewId(@PathParam("reviewId") long reviewId)
    {
        try
        {

            Review r = reviewSessionBeanLocal.retrieveReviewByReviewId(reviewId);
            
            if (r.getDeal() != null) {
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
                
                if(r.getCustomer() != null) {
                    r.getCustomer().getRedemptions().clear();
                    r.getCustomer().getFavourites().clear();
                    r.getCustomer().getReviews().clear();
                    r.getCustomer().getDeals().clear();
                    r.getCustomer().getSaleTransactions().clear();
                }
            }
            
            return Response.status(Status.OK).entity(r).build();
        }
        catch(ReviewNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReview(Review review)
    {
        if(review != null)
        {
            try
            {                
                System.out.println("********** ReviewResource.updateReview(): Review " + review.getReviewId() + " is being updated.");
                
                reviewSessionBeanLocal.updateReview(review);
                
                return Response.status(Response.Status.OK).build();
            }
            catch(ReviewNotFoundException ex)
            {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
            catch(Exception ex)
            {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update review request").build();
        }
    }
    
    @Path("{reviewId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReview(@PathParam("reviewId") Long reviewId)
    {
        try
        {
            reviewSessionBeanLocal.deleteReview(reviewId);
            
            return Response.status(Status.OK).build();
        }
        catch(ReviewNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
}
