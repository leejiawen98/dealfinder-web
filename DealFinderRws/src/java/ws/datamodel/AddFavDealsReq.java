/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

/**
 *
 * @author stellaang
 */
public class AddFavDealsReq {
    
    private long customerId;
    private long dealId;

    public AddFavDealsReq() {
    }

    public AddFavDealsReq(long customerId, long dealId) {
        this.customerId = customerId;
        this.dealId = dealId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getDealId() {
        return dealId;
    }

    public void setDealId(long dealId) {
        this.dealId = dealId;
    }
    
    
    
}
