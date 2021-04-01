/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
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
import util.exception.DeleteCustomerException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author stellaang
 */
@Path("Customer")
public class CustomerResource {
    
    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;

    private final CustomerSessionBeanLocal customerSessionBeanLocal;

    public CustomerResource() {
        
        sessionBeanLookup = new SessionBeanLookup();
        
        customerSessionBeanLocal = sessionBeanLookup.lookupCustomerSessionBeanLocal();
        
    }
    
    @Path("retrieveAllCustomers")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCustomer()
    {
        try
        {
            List<Customer> customer = customerSessionBeanLocal.getAllCustomers();
            
            GenericEntity<List<Customer>> genericEntity = new GenericEntity<List<Customer>>(customer) {
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
    public Response createNewCustomer(Customer newCustomer) {
        if (newCustomer != null) {
            try { 
                Long newCustomerId = customerSessionBeanLocal.createCustomer(newCustomer);

                return Response.status(Response.Status.OK).entity(newCustomerId).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }
    
    @Path("customerLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerLogin(@QueryParam("username") String username, 
                                @QueryParam("password") String password)
    {
        try
        {
            Customer customer = customerSessionBeanLocal.customerLogin(username, password);
            System.out.println("********** CustomerResource.customerLogin(): Customer " + customer.getUsername() + " login remotely via web service");

            customer.setPassword(null);
            customer.setSalt(null);     
            customer.getSaleTransactions().clear();
            
            return Response.status(Status.OK).entity(customer).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveCustomerByCustomerId/{customerId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomerByCustomerId(@PathParam("customerId") long customerId)
    {
        try
        {

            Customer customer = customerSessionBeanLocal.getCustomerByCustomerId(customerId);
            
            customer.setPassword(null);
            customer.setSalt(null);            
            customer.getSaleTransactions().clear();
            
            return Response.status(Status.OK).entity(customer).build();
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
    
    @Path("retrieveCustomerByUsername/{username}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomerByUsername(@PathParam("username") String username)
    {
        try
        {

            Customer customer = customerSessionBeanLocal.getCustomerByUsername(username);
            
            customer.setPassword(null);
            customer.setSalt(null);            
            customer.getSaleTransactions().clear();
            
            return Response.status(Status.OK).entity(customer).build();
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
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(Customer customer)
    {
        if(customer != null)
        {
            try
            {                
                System.out.println("********** CustomerResource.updateCustomer(): Customer " + customer.getUsername() + " is being updated.");
                
                customerSessionBeanLocal.updateCustomer(customer);
                
                return Response.status(Response.Status.OK).build();
            }
            catch(UpdateCustomerException ex)
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
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update product request").build();
        }
    }
    
    @Path("{customerId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCustomer(@PathParam("customerId") Long customerId)
    {
        try
        {
            customerSessionBeanLocal.deleteCustomer(customerId);
            
            return Response.status(Status.OK).build();
        }
        catch(CustomerNotFoundException | DeleteCustomerException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    
}
