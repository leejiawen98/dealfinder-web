/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.Redemption;

/**
 *
 * @author stellaang
 */
public class CreateRedemptionReq {
    
    private long customerId;
    private long dealId;
    private Redemption redemption;

    public CreateRedemptionReq() {
    }

    public CreateRedemptionReq(long customerId, long dealId, Redemption redemption) {
        this.customerId = customerId;
        this.dealId = dealId;
        this.redemption = redemption;
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

    public Redemption getRedemption() {
        return redemption;
    }

    public void setRedemption(Redemption redemption) {
        this.redemption = redemption;
    }
    
    
    
    
}
