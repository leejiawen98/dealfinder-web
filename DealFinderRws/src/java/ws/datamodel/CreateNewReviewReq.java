/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.Review;

/**
 *
 * @author Aaron Tan
 */
public class CreateNewReviewReq {
 
    private Review newReview;
    private long dealId;
    private long customerId;

    public CreateNewReviewReq() {
    }

    public CreateNewReviewReq(Review newReview, long dealId, long customerId) {
        this.newReview = newReview;
        this.dealId = dealId;
        this.customerId = customerId;
    }

    public Review getNewReview() {
        return newReview;
    }

    public void setNewReview(Review newReview) {
        this.newReview = newReview;
    }

    public long getDealId() {
        return dealId;
    }

    public void setDealId(long dealId) {
        this.dealId = dealId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
    
}
