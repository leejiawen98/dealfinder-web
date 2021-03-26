/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author yeerouhew
 */
@Entity
public class Customer extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String firstName;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String lastName;
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal eWalletAmount;
    
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Deal> favDeals;
    
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Deal> deals;
    
    @OneToOne(mappedBy = "customer", fetch = FetchType.LAZY)
    private CreditCard creditCard;

    public Customer() {
        favDeals = new ArrayList<>();
        deals = new ArrayList<>();
    }

    public Customer(String firstName, String lastName, BigDecimal eWalletAmount, String username, String password, String email, String mobileNum) {
        super(username, password, email, mobileNum);
        this.firstName = firstName;
        this.lastName = lastName;
        this.eWalletAmount = eWalletAmount;
        
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + id + " ]";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal geteWalletAmount() {
        return eWalletAmount;
    }

    public void seteWalletAmount(BigDecimal eWalletAmount) {
        this.eWalletAmount = eWalletAmount;
    }

    public List<Deal> getFavDeals() {
        return favDeals;
    }

    public void setFavDeals(List<Deal> favDeals) {
        this.favDeals = favDeals;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
    
    
}
