/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.CategorySessionBeanLocal;
import entity.Category;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import util.exception.CategoryNotFoundException;

/**
 *
 * @author stellaang
 */

@Path("Category")
public class CategoryResource {
    
    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;

    private final CategorySessionBeanLocal categorySessionBeanLocal;

    public CategoryResource() {
        
        sessionBeanLookup = new SessionBeanLookup();
        
        categorySessionBeanLocal = sessionBeanLookup.lookupCategorySessionBeanLocal();
        
    }
    
    @Path("retrieveAllCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategories()
    {
        try
        {
            List<Category> category = categorySessionBeanLocal.retrieveAllCategories();
            
            GenericEntity<List<Category>> genericEntity = new GenericEntity<List<Category>>(category) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllRootCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllRootCategories()
    {
        try
        {
            List<Category> category = categorySessionBeanLocal.retrieveAllRootCategories();
            
            GenericEntity<List<Category>> genericEntity = new GenericEntity<List<Category>>(category) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllLeafCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllLeafCategories()
    {
        try
        {
            List<Category> category = categorySessionBeanLocal.retrieveAllLeafCategories();
            
            GenericEntity<List<Category>> genericEntity = new GenericEntity<List<Category>>(category) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllCategoriesWithoutProduct")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategoriesWithoutProduct()
    {
        try
        {
            List<Category> category = categorySessionBeanLocal.retrieveAllCategories();
            
            GenericEntity<List<Category>> genericEntity = new GenericEntity<List<Category>>(category) {
            };
            
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        }
        catch(Exception ex)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveCategoryByCategoryId/{categoryId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCategoryByCategoryId(@PathParam("categoryId") long categoryId)
    {
        try
        {

            Category category = categorySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
            
            category.getDeals().clear();
            category.getParentCategory();
            category.getSubCategories().clear();
            
            return Response.status(Status.OK).entity(category).build();
        }
        catch(CategoryNotFoundException ex)
        {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
}
