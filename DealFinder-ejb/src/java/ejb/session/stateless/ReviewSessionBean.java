/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Deal;
import entity.Review;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.BusinessNotFoundException;
import util.exception.CreateNewReviewException;
import util.exception.CustomerNotFoundException;
import util.exception.DealNotFoundException;
import util.exception.DeleteReviewException;
import util.exception.ReviewNotFoundException;

/**
 *
 * @author yeerouhew
 */
@Stateless
public class ReviewSessionBean implements ReviewSessionBeanLocal {

    @EJB
    private DealSessionBeanLocal dealSessionBeanLocal;
    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;
    
   
    @Override
    public Review createNewReview(Long customerId, Review newReview, Long dealId) throws CreateNewReviewException
    {
        if(newReview != null)
        {
            try 
            {
                Customer customerEntity = customerSessionBeanLocal.getCustomerByCustomerId(customerId);
                newReview.setCustomer(customerEntity);
                customerEntity.getReviews().add(newReview);
                
                Deal deal = dealSessionBeanLocal.retrieveDealByDealId(dealId);
                newReview.setDeal(deal);
                deal.getReviews().add(newReview);
                
                em.persist(newReview);
                em.flush();
                
                return newReview;
            } 
            catch (CustomerNotFoundException | DealNotFoundException ex) 
            {
                throw new CreateNewReviewException(ex.getMessage());
            } 
        }
        else
        {
            throw new CreateNewReviewException("Review information not provided.");
        }
    }
    
    @Override
    public List<Review> retrieveAllReviews()
    {
        Query query = em.createQuery("SELECT r FROM Review r");
        return query.getResultList();
    }

    @Override
    public List<Review> retrieveReviewsByDealId(Long dealId) throws DealNotFoundException 
    {
        Query query = em.createQuery("SELECT r FROM Review r WHERE r.deal.dealId =:inDealId");
        query.setParameter("inDealId", dealId);
        
        try {
            return query.getResultList();

        } catch (NoResultException | NonUniqueResultException ex) {
            throw new DealNotFoundException("Deal Id " + dealId + " does not exist!");
        }
    }
    
    public Review retrieveReviewByReviewId(Long reviewId) throws ReviewNotFoundException
    {
        Review review = em.find(Review.class, reviewId);
        
        if(review != null)
        {
            review.getCustomer();
            review.getDeal();
            
            return review;
        }
        else 
        {
            throw new ReviewNotFoundException("Review Id " + reviewId + " does not exist!");
        }
    }
    
    //dk if this is needed
    @Override
    public void deleteReview(Review review) throws DeleteReviewException
    {
        try 
        {
            Review reviewToRemove = retrieveReviewByReviewId(review.getReviewId());
            reviewToRemove.getDeal().getReviews().remove(review);
            reviewToRemove.getCustomer().getReviews().remove(review);
            
            em.remove(reviewToRemove);
        } 
        catch (ReviewNotFoundException ex) 
        {
            throw new DeleteReviewException("An error has occurred while deleting Review " + review.getReviewId());
        }
    }
    
    @Override
    public List<Review> retrieveReviewByBusinessId(Long businessId) throws BusinessNotFoundException
    {
        Query query = em.createQuery("SELECT r FROM Review r WHERE r.deal.business.id =:inBusinessId");
        query.setParameter("inBusinessId", businessId);
        
        try {
            return query.getResultList();

        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BusinessNotFoundException("Business Id " + businessId + " does not exist!");
        }
    }
    
}
