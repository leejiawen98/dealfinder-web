/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Category;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;

/**
 *
 * @author Aaron Tan
 */
@Stateless
public class CategorySessionBean implements CategorySessionBeanLocal {

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
   
    
    public CategorySessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    @Override
    public Category createNewCategoryEntity(Category newCategoryEntity, Long parentCategoryId) throws InputDataValidationException, CreateNewCategoryException
    {
        Set<ConstraintViolation<Category>>constraintViolations = validator.validate(newCategoryEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                if(parentCategoryId != null)
                {
                    Category parentCategoryEntity = retrieveCategoryByCategoryId(parentCategoryId);

                    if(!parentCategoryEntity.getDeals().isEmpty())
                    {
                        throw new CreateNewCategoryException("Parent category cannot be associated with any product");
                    }

                    newCategoryEntity.setParentCategory(parentCategoryEntity);
                }
                
                em.persist(newCategoryEntity);
                em.flush();

                return newCategoryEntity;
            }
            catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewCategoryException("Category with same name already exist");
                }
                else
                {
                    throw new CreateNewCategoryException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
            catch(Exception ex)
            {
                throw new CreateNewCategoryException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<Category> retrieveAllCategories()
    {
        Query query = em.createQuery("SELECT c FROM Category c ORDER BY c.name ASC");
        List<Category> categoryEntities = query.getResultList();
        
        for(Category categoryEntity:categoryEntities)
        {
            categoryEntity.getParentCategory();
            categoryEntity.getSubCategories().size();
            categoryEntity.getDeals().size();
        }
        
        return categoryEntities;
    }
    
    @Override
    public List<Category> retrieveAllRootCategories()
    {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.parentCategory IS NULL ORDER BY c.name ASC");
        List<Category> rootCategoryEntities = query.getResultList();
        
        for(Category rootCategoryEntity:rootCategoryEntities)
        {            
            lazilyLoadSubCategories(rootCategoryEntity);
            
            rootCategoryEntity.getDeals().size();
        }
        
        return rootCategoryEntities;
    }
    
    @Override
    public List<Category> retrieveAllLeafCategories()
    {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.subCategories IS EMPTY ORDER BY c.name ASC");
        List<Category> leafCategoryEntities = query.getResultList();
        
        for(Category leafCategoryEntity:leafCategoryEntities)
        {
            leafCategoryEntity.getParentCategory();
            leafCategoryEntity.getDeals().size();
        }
        
        return leafCategoryEntities;
    }
    
    @Override
    public List<Category> retrieveAllCategoriesWithoutProduct()
    {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.deals IS EMPTY ORDER BY c.name ASC");
        List<Category> rootCategoryEntities = query.getResultList();
        
        for(Category rootCategoryEntity:rootCategoryEntities)
        {
            rootCategoryEntity.getParentCategory();            
        }
        
        return rootCategoryEntities;
    }
    
    @Override
    public Category retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException
    {
        Category categoryEntity = em.find(Category.class, categoryId);
        
        if(categoryEntity != null)
        {
            return categoryEntity;
        }
        else
        {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exist!");
        }               
    }
    
    @Override
    public void updateCategory(Category categoryEntity, Long parentCategoryId) throws InputDataValidationException, CategoryNotFoundException, UpdateCategoryException
    {
        Set<ConstraintViolation<Category>>constraintViolations = validator.validate(categoryEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(categoryEntity.getCategoryId()!= null)
            {
                Category categoryEntityToUpdate = retrieveCategoryByCategoryId(categoryEntity.getCategoryId());
                
                Query query = em.createQuery("SELECT c FROM Category c WHERE c.name = :inName AND c.categoryId <> :inCategoryId");
                query.setParameter("inName", categoryEntity.getName());
                query.setParameter("inCategoryId", categoryEntity.getCategoryId());
                
                if(!query.getResultList().isEmpty())
                {
                    throw new UpdateCategoryException("The name of the category to be updated is duplicated");
                }
                
                categoryEntityToUpdate.setName(categoryEntity.getName());
                categoryEntityToUpdate.setDescription(categoryEntity.getDescription());                               
                
                if(parentCategoryId != null)
                {
                    if(categoryEntityToUpdate.getCategoryId().equals(parentCategoryId))
                    {
                        throw new UpdateCategoryException("Category cannot be its own parent");
                    }
                    else if(categoryEntityToUpdate.getParentCategory() == null || (!categoryEntityToUpdate.getParentCategory().getCategoryId().equals(parentCategoryId)))
                    {
                        Category parentCategoryEntityToUpdate = retrieveCategoryByCategoryId(parentCategoryId);
                        
                        if(!parentCategoryEntityToUpdate.getDeals().isEmpty())
                        {
                            throw new UpdateCategoryException("Parent category cannot have any product associated with it");
                        }
                        
                        categoryEntityToUpdate.setParentCategory(parentCategoryEntityToUpdate);
                    }
                }
                else
                {
                    categoryEntityToUpdate.setParentCategory(null);
                }                
            }
            else
            {
                throw new CategoryNotFoundException("Category ID not provided for category to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException
    {
        Category categoryEntityToRemove = retrieveCategoryByCategoryId(categoryId);
        
        if(!categoryEntityToRemove.getSubCategories().isEmpty())
        {
            throw new DeleteCategoryException("Category ID " + categoryId + " is associated with existing sub-categories and cannot be deleted!");
        }
        else if(!categoryEntityToRemove.getDeals().isEmpty())
        {
            throw new DeleteCategoryException("Category ID " + categoryId + " is associated with existing products and cannot be deleted!");
        }
        else
        {
            categoryEntityToRemove.setParentCategory(null);
            
            em.remove(categoryEntityToRemove);
        }                
    }
    
    private void lazilyLoadSubCategories(Category categoryEntity)
    {
        for(Category ce:categoryEntity.getSubCategories())
        {
            lazilyLoadSubCategories(ce);
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Category>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
