/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Review;
import java.util.List;
import javax.ejb.Local;
import util.exception.BusinessNotFoundException;
import util.exception.CreateNewReviewException;
import util.exception.DealNotFoundException;
import util.exception.DeleteReviewException;

/**
 *
 * @author yeerouhew
 */
@Local
public interface ReviewSessionBeanLocal {

    public Review createNewReview(Long customerId, Review newReview, Long dealId) throws CreateNewReviewException;

    public List<Review> retrieveAllReviews();

    public List<Review> retrieveReviewsByDealId(Long dealId) throws DealNotFoundException;

    public void deleteReview(Review review) throws DeleteReviewException;

    public List<Review> retrieveReviewByBusinessId(Long businessId) throws BusinessNotFoundException;
    
}
