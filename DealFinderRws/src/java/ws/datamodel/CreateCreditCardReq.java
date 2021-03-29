/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.CreditCard;

/**
 *
 * @author stellaang
 */
public class CreateCreditCardReq {
    
    private long customerId;
    private CreditCard creditCard;

    public CreateCreditCardReq() {
    }

    public CreateCreditCardReq(long customerId, CreditCard creditCard) {
        this.customerId = customerId;
        this.creditCard = creditCard;
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
     * @return the creditCard
     */
    public CreditCard getCreditCard() {
        return creditCard;
    }

    /**
     * @param creditCard the creditCard to set
     */
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
    
}
