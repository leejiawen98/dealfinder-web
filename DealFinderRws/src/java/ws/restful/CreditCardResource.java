/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.CreditCardSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.CreditCard;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import util.exception.CreateNewCreditCardException;
import util.exception.CreditCardNotFoundException;
import ws.datamodel.CreateCreditCardReq;

/**
 *
 * @author stellaang
 */
@Path("CreditCard")
public class CreditCardResource {
    
    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;

    private final CreditCardSessionBeanLocal creditCardSessionBeanLocal;

    public CreditCardResource() {
        
        sessionBeanLookup = new SessionBeanLookup();
        
        creditCardSessionBeanLocal = sessionBeanLookup.lookupCreditCardSessionBeanLocal();
        
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewCreditCardEntityForCustomer(CreateCreditCardReq createCreditCardReq) {
        if (createCreditCardReq != null) {
            try { 
                CreditCard creditCard = creditCardSessionBeanLocal.createNewCreditCardEntityForCustomer(createCreditCardReq.getCreditCard(), createCreditCardReq.getCustomerId());

                return Response.status(Response.Status.OK).entity(creditCard).build();
            } catch (CreateNewCreditCardException ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }
    
    @Path("retrieveCreditCardByCreditCardId/{creditCardId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCardByCreditCardId(@PathParam("creditCardId") long creditCardId)
    {
        try
        {

            CreditCard creditCard = creditCardSessionBeanLocal.retrieveCreditCardByCreditCardId(creditCardId);
            
            creditCard.getCustomer();
            
            return Response.status(Status.OK).entity(creditCard).build();
        }
        catch(CreditCardNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }    
    
    @Path("retrieveCreditCardByCustomerId/{customerId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCardByCustomerId(@PathParam("customerId") long customerId)
    {
        try
        {

            CreditCard creditCard = creditCardSessionBeanLocal.retrieveCreditCardByCustomerId(customerId);
            
            creditCard.setCcName(null);
            
            return Response.status(Status.OK).entity(creditCard).build();
        }
        catch(CreditCardNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("{creditCardId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCard(@PathParam("creditCardId") Long creditCardId)
    {
        try
        {
            creditCardSessionBeanLocal.deleteCreditCard(creditCardId);
            
            return Response.status(Status.OK).build();
        }
        catch(CreditCardNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    
    
}
