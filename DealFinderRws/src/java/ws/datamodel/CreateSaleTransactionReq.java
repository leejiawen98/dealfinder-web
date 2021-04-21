/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.SaleTransaction;

/**
 *
 * @author stellaang
 */
public class CreateSaleTransactionReq {
    
    private long customerId;
    private SaleTransaction saleTransaction;
    private long dealId;

    public CreateSaleTransactionReq() {
    }

    public CreateSaleTransactionReq(long customerId, SaleTransaction saleTransaction) {
        this.customerId = customerId;
        this.saleTransaction = saleTransaction;
    }

    /**
     * @return the customerId
     */
    public long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the saleTransaction
     */
    public SaleTransaction getSaleTransaction() {
        return saleTransaction;
    }

    /**
     * @param saleTransaction the saleTransaction to set
     */
    public void setSaleTransaction(SaleTransaction saleTransaction) {
        this.saleTransaction = saleTransaction;
    }

    public long getDealId() {
        return dealId;
    }

    public void setDealId(long dealId) {
        this.dealId = dealId;
    }
    
}
