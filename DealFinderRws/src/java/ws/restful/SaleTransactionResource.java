/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.SaleTransactionSessionBeanLocal;
import entity.Customer;
import entity.SaleTransaction;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.DealNotFoundException;
import util.exception.SaleTransactionNotFoundException;
import ws.datamodel.CreateSaleTransactionReq;

/**
 *
 * @author stellaang
 */
@Path("SaleTransaction")
public class SaleTransactionResource {
    
    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;

    private final SaleTransactionSessionBeanLocal saleTransactionSessionBeanLocal;

    public SaleTransactionResource() {
        
        sessionBeanLookup = new SessionBeanLookup();
        
        saleTransactionSessionBeanLocal = sessionBeanLookup.lookupSaleTransactionSessionBeanLocal();
        
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewSaleTransaction(CreateSaleTransactionReq createSaleTransactionReq) {
        if (createSaleTransactionReq != null) {
            try { 
                SaleTransaction newSaleTransaction = saleTransactionSessionBeanLocal.createNewSaleTransaction(createSaleTransactionReq.getCustomerId(), createSaleTransactionReq.getSaleTransaction(), createSaleTransactionReq.getDealId());
                return Response.status(Response.Status.OK).entity(newSaleTransaction).build();
            } catch (CustomerNotFoundException | CreateNewSaleTransactionException ex) 
            {
                return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }
    
    @Path("retrieveAllSaleTransactions")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllSaleTransactions()
    {
        try
        {
            List<SaleTransaction> saleTransactions = saleTransactionSessionBeanLocal.retrieveAllSaleTransactions();
            
            for(SaleTransaction s: saleTransactions) {
                if(s.getUser() != null) {
                    s.getUser().getSaleTransactions().clear();
                    Customer c = (Customer) s.getUser();
                    c.getRedemptions().clear();
                    c.getFavourites().clear();
                    c.getReviews().clear();
                    c.getDeals().clear();
                    c.getSaleTransactions().clear();
                    c.setCreditCard(null);
                    
                    if (s.getDeal() != null) {
                        s.getDeal().getSaleTransactions().clear();
                        if (s.getDeal().getBusiness() != null) {
                            s.getDeal().getBusiness().getSaleTransactions().clear();
                            s.getDeal().getBusiness().getDeals().clear();
                        }
                        if (s.getDeal().getCategory() != null) {
                            s.getDeal().getCategory().getDeals().clear();
                            s.getDeal().getCategory().getSubCategories().clear();
                            if (s.getDeal().getCategory().getParentCategory() != null) {
                                s.getDeal().getCategory().getParentCategory().getSubCategories().clear();
                            }
                        }
                        
                        s.getDeal().getRedemptions().clear();
                        s.getDeal().getCustomers().clear();
                        s.getDeal().getFavourites().clear();
                        s.getDeal().getReviews().clear();
                        s.getDeal().getTags().clear();
                    }
                }
                
            }
            
            GenericEntity<List<SaleTransaction>> genericEntity = new GenericEntity<List<SaleTransaction>>(saleTransactions) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveSaleTransactionBySaleTransactionId/{saleTransactionId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSaleTransactionBySaleTransactionId(@PathParam("saleTransactionId") long saleTransactionId)
    {
        try
        {

            SaleTransaction saleTransaction = saleTransactionSessionBeanLocal.retrieveSaleTransactionBySaleTransactionId(saleTransactionId);
            
                    Customer c = (Customer) saleTransaction.getUser();
                    c.getRedemptions().clear();
                    c.getFavourites().clear();
                    c.getReviews().clear();
                    c.getDeals().clear();
                    c.getSaleTransactions().clear();

            if (saleTransaction.getDeal() != null) {
                saleTransaction.getDeal().getSaleTransactions().clear();
                if (saleTransaction.getDeal().getBusiness() != null) {
                    saleTransaction.getDeal().getBusiness().getSaleTransactions().clear();
                    saleTransaction.getDeal().getBusiness().getDeals().clear();
                }
                if (saleTransaction.getDeal().getCategory() != null) {
                    saleTransaction.getDeal().getCategory().getDeals().clear();
                    saleTransaction.getDeal().getCategory().getSubCategories().clear();
                    if (saleTransaction.getDeal().getCategory().getParentCategory() != null) {
                        saleTransaction.getDeal().getCategory().getParentCategory().getSubCategories().clear();
                    }
                }
                saleTransaction.getDeal().getRedemptions().clear();
                saleTransaction.getDeal().getCustomers().clear();
                saleTransaction.getDeal().getFavourites().clear();
                saleTransaction.getDeal().getReviews().clear();
                saleTransaction.getDeal().getTags().clear();
            }

            return Response.status(Status.OK).entity(saleTransaction).build();
        }
        catch(SaleTransactionNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveSaleTransactionDealByCustomerId/{customerId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSaleTransactionDealByCustomerId(@PathParam("customerId") long customerId)
    {
        try
        {
            List<SaleTransaction> saleTransactions = saleTransactionSessionBeanLocal.retrieveSaleTransactionDealByCustomerId(customerId);
            
            for (SaleTransaction s : saleTransactions) {
                s.getDeal().setBusiness(null);
                s.getDeal().setCategory(null);
                s.getDeal().getCustomers().clear();
                s.getDeal().getFavourites().clear();
                s.getDeal().getRedemptions().clear();
                s.getDeal().getReviews().clear();
                s.getDeal().getSaleTransactions().clear();
                s.getDeal().getTags().clear();
                    s.setUser(null);
            }
            
            GenericEntity<List<SaleTransaction>> genericEntity = new GenericEntity<List<SaleTransaction>>(saleTransactions) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(CustomerNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveSaleTransactionDealByDealId/{dealId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSaleTransactionDealByDealId(@PathParam("dealId") long dealId)
    {
        try
        {
            List<SaleTransaction> saleTransactions = saleTransactionSessionBeanLocal.retrieveSaleTransactionDealByDealId(dealId);
            
            for (SaleTransaction s : saleTransactions) {
                if (s.getUser() != null) {
                    s.getUser().getSaleTransactions().clear();
                    Customer c = (Customer) s.getUser();
                    c.getRedemptions().clear();
                    c.getFavourites().clear();
                    c.getReviews().clear();
                    c.getDeals().clear();
                    c.getSaleTransactions().clear();
                    if (s.getDeal() != null) {
                        s.getDeal().getSaleTransactions().clear();
                        if (s.getDeal().getBusiness() != null) {
                            s.getDeal().getBusiness().getSaleTransactions().clear();
                            s.getDeal().getBusiness().getDeals().clear();
                        }
                        if (s.getDeal().getCategory() != null) {
                            s.getDeal().getCategory().getDeals().clear();
                            s.getDeal().getCategory().getSubCategories().clear();
                            if (s.getDeal().getCategory().getParentCategory() != null) {
                                s.getDeal().getCategory().getParentCategory().getSubCategories().clear();
                            }
                        }
                        s.getDeal().getRedemptions().clear();
                        s.getDeal().getCustomers().clear();
                        s.getDeal().getFavourites().clear();
                        s.getDeal().getReviews().clear();
                        s.getDeal().getTags().clear();
                    }
                }
            }
            
            GenericEntity<List<SaleTransaction>> genericEntity = new GenericEntity<List<SaleTransaction>>(saleTransactions) {
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
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<SaleTransaction>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    
}
