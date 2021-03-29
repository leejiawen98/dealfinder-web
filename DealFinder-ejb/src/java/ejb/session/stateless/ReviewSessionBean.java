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
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewReviewException;
import util.exception.DealNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ReviewExistException;
import util.exception.ReviewNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Aaron Tan
 */
@Stateless
public class ReviewSessionBean implements ReviewSessionBeanLocal {

    @EJB(name = "DealSessionBeanLocal")
    private DealSessionBeanLocal dealSessionBeanLocal;

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ReviewSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Review createNewReview(Review newReview, Long dealId) throws ReviewExistException, UnknownPersistenceException, InputDataValidationException, CreateNewReviewException {

        Set<ConstraintViolation<Review>> constraintViolations = validator.validate(newReview);

        if (constraintViolations.isEmpty()) {
            try {
                if (dealId == null) {
                    throw new CreateNewReviewException("A new review must be associated with a deal");
                }

                Deal deal = dealSessionBeanLocal.retrieveDealByDealId(dealId);

                newReview.setDeal(deal);
                deal.getReviews().add(newReview);

                em.persist(newReview);
                em.flush();

                return newReview;

            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ReviewExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } catch (DealNotFoundException ex) {
                throw new CreateNewReviewException("An error has occured while creating the new review: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<Review> retrieveAllReviews() {
        Query query = em.createQuery("SELECT r FROM Review r ORDER BY r.dealRating DESC");
        List<Review> reviews = query.getResultList();

        for (Review review : reviews) {
            review.getDeal();
        }
        return reviews;
    }
    
    @Override
    public Review retrieveReviewByReviewId(Long reviewId) throws ReviewNotFoundException {
        Review review = em.find(Review.class, reviewId);

        if (review != null) {
            review.getDeal();
            return review;
        } else {
            throw new ReviewNotFoundException("Review ID " + reviewId + " does not exist!");
        }
    }
    
    @Override
    public List<Review> retrieveReviewsByDealId(Long dealId) throws DealNotFoundException {
        Deal deal = em.find(Deal.class, dealId);

        if (deal != null) {            
            List<Review> reviews = deal.getReviews();
            
            return reviews;
        } else {
            throw new DealNotFoundException("Deal Id " + dealId + " does not exist!");
        }
    }
    
    @Override
    public void updateReview(Review review) throws ReviewNotFoundException, InputDataValidationException {
        if (review != null && review.getReviewId() != null) {
            Set<ConstraintViolation<Review>> constraintViolations = validator.validate(review);

            if (constraintViolations.isEmpty()) {
                Review reviewToUpdate = retrieveReviewByReviewId(review.getReviewId());

                reviewToUpdate.setDeal(review.getDeal());
                reviewToUpdate.setDescription(review.getDescription());
                reviewToUpdate.setDealRating(review.getDealRating());
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new ReviewNotFoundException("Review ID was not provided for the review to be updated");
        }
    }
    
    @Override
    public void deleteReview(Long reviewId) throws ReviewNotFoundException {
        Review reviewToRemove = retrieveReviewByReviewId(reviewId);
        
        if (reviewToRemove.getDeal() != null){
            Deal deal = reviewToRemove.getDeal();
            deal.getReviews().remove(reviewToRemove);
            reviewToRemove.setDeal(null);
        }

        em.remove(reviewToRemove);
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Review>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}