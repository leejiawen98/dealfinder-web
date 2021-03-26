/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Tag;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import util.exception.CreateNewTagException;
import util.exception.DeleteTagException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;
import util.exception.UpdateTagException;


/**
 *
 * @author Aaron Tan
 */
@Stateless
@Local(TagSessionBeanLocal.class)
public class TagSessionBean implements TagSessionBeanLocal {

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
           
    public TagSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    @Override
    public Tag createNewTagEntity(Tag newTagEntity) throws InputDataValidationException, CreateNewTagException
    {
        Set<ConstraintViolation<Tag>>constraintViolations = validator.validate(newTagEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newTagEntity);
                em.flush();

                return newTagEntity;
            }
            catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewTagException("Tag with same name already exist");
                }
                else
                {
                    throw new CreateNewTagException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
            catch(Exception ex)
            {                
                throw new CreateNewTagException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    @Override
    public List<Tag> retrieveAllTags()
    {
        Query query = em.createQuery("SELECT t FROM TagEntity t ORDER BY t.name ASC");
        List<Tag> tagEntities = query.getResultList();
        
        for(Tag tagEntity:tagEntities)
        {            
            tagEntity.getDeals().size();
        }
        
        return tagEntities;
    }
    
        
    @Override
    public Tag retrieveTagByTagId(Long tagId) throws TagNotFoundException
    {
        Tag tagEntity = em.find(Tag.class, tagId);
        
        if(tagEntity != null)
        {
            return tagEntity;
        }
        else
        {
            throw new TagNotFoundException("Tag ID " + tagId + " does not exist!");
        }               
    }
    
        
    @Override
    public void updateTag(Tag tagEntity) throws InputDataValidationException, TagNotFoundException, UpdateTagException
    {
        Set<ConstraintViolation<Tag>>constraintViolations = validator.validate(tagEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(tagEntity.getTagId()!= null)
            {
                Tag tagEntityToUpdate = retrieveTagByTagId(tagEntity.getTagId());
                
                Query query = em.createQuery("SELECT t FROM TagEntity t WHERE t.name = :inName AND t.tagId <> :inTagId");
                query.setParameter("inName", tagEntity.getName());
                query.setParameter("inTagId", tagEntity.getTagId());
                
                if(!query.getResultList().isEmpty())
                {
                    throw new UpdateTagException("The name of the tag to be updated is duplicated");
                }
                
                tagEntityToUpdate.setName(tagEntity.getName());                               
            }
            else
            {
                throw new TagNotFoundException("Tag ID not provided for tag to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
        
    @Override
    public void deleteTag(Long tagId) throws TagNotFoundException, DeleteTagException
    {
        Tag tagEntityToRemove = retrieveTagByTagId(tagId);
        
        if(!tagEntityToRemove.getDeals().isEmpty())
        {
            throw new DeleteTagException("Tag ID " + tagId + " is associated with existing products and cannot be deleted!");
        }
        else
        {
            em.remove(tagEntityToRemove);
        }                
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Tag>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
