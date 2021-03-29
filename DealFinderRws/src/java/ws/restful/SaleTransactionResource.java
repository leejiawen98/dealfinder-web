/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.SaleTransactionSessionBeanLocal;
import entity.SaleTransaction;
import java.util.List;
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
                SaleTransaction newSaleTransaction = saleTransactionSessionBeanLocal.createNewSaleTransaction(createSaleTransactionReq.getCustomerId(), createSaleTransactionReq.getSaleTransaction());

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
            
            saleTransaction.getUser();
            
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
    
    @Path("retrieveSaleTransactionDealByDealId/{dealId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSaleTransactionDealByDealId(@PathParam("dealId") long dealId)
    {
        try
        {
            List<SaleTransaction> saleTransactions = saleTransactionSessionBeanLocal.retrieveSaleTransactionDealByDealId(dealId);
            
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
    
}
