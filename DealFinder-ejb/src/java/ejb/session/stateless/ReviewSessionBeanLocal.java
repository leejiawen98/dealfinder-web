/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Review;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface ReviewSessionBeanLocal {
    public Review createNewReview(Review newReview, Long dealId) throws ReviewExistException, UnknownPersistenceException, InputDataValidationException, CreateNewReviewException;
    
    public List<Review> retrieveAllReviews();
    
    public Review retrieveReviewByReviewId(Long reviewId) throws ReviewNotFoundException;
    
    public List<Review> retrieveReviewsByDealId(Long dealId) throws DealNotFoundException;
    
    public void updateReview(Review review) throws ReviewNotFoundException, InputDataValidationException;
    
    public void deleteReview(Long reviewId) throws ReviewNotFoundException;
}