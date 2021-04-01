/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.AdminSessionBeanLocal;
import entity.Admin;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author stellaang
 */
@Path("Admin")
public class AdminResource {
    
    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;

    private final AdminSessionBeanLocal adminSessionBeanLocal;

    public AdminResource() {
        
        sessionBeanLookup = new SessionBeanLookup();
        
        adminSessionBeanLocal = sessionBeanLookup.lookupAdminSessionBeanLocal();
        
    }
    
    @Path("retrieveAllAdmins")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllAdmin()
    {
        try
        {
            List<Admin> admin = adminSessionBeanLocal.getAllAdmins();
            
            GenericEntity<List<Admin>> genericEntity = new GenericEntity<List<Admin>>(admin) {
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
    public Response createNewAdmin(Admin newAdmin) {
        if (newAdmin != null) {
            try { 
                Long newAdminId = adminSessionBeanLocal.createAdmin(newAdmin);

                return Response.status(Response.Status.OK).entity(newAdminId).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new record request").build();
        }
    }
    
}
