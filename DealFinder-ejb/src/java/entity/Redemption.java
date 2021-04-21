/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author yeerouhew
 */
@Entity
public class Redemption implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long redemptionId;
    @Column(nullable = false, length = 32)
    @NotNull
    private Boolean redeemed;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Deal deal;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Customer customer;

    public Redemption() {
        this.redeemed = false;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (redemptionId != null ? redemptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the redemptionId fields are not set
        if (!(object instanceof Redemption)) {
            return false;
        }
        Redemption other = (Redemption) object;
        if ((this.redemptionId == null && other.redemptionId != null) || (this.redemptionId != null && !this.redemptionId.equals(other.redemptionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Redemption[ id=" + redemptionId + " ]";
    }
    
    public Long getRedemptionId() {
        return redemptionId;
    }

    public void setRedemptionId(Long redemptionId) {
        this.redemptionId = redemptionId;
    }

    public Boolean getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(Boolean redeemed) {
        this.redeemed = redeemed;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    

}
