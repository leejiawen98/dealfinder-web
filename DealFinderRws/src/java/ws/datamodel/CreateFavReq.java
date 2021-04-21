/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.Favourite;

/**
 *
 * @author stellaang
 */
public class CreateFavReq {
    
    private long customerId;
    private long dealId;
    
    private Favourite favourite;

    public CreateFavReq() {
    }

    public CreateFavReq(long customerId, long dealId, Favourite favourite) {
        this.customerId = customerId;
        this.dealId = dealId;
        this.favourite = favourite;
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

    public Favourite getFavourite() {
        return favourite;
    }

    public void setFavourite(Favourite favourite) {
        this.favourite = favourite;
    }
    
    

    
}
