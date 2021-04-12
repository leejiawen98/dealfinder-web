/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author yeerouhew
 */
@Entity
public class Deal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dealId;
    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    private String serialNum;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String dealName;
    @Column(nullable = false, length = 128)
    @NotNull
    @Size(max = 128)
    private String description;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date startDateTime;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date endDateTime;
    @Column(nullable = false)
    @Min(0)
    private Integer quantityLeft;
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal unitPrice;
   
    private boolean redeemed;
    private boolean enabled;
    
    
    @OneToMany(mappedBy = "deal", fetch = FetchType.LAZY)
    private List<SaleTransaction> saleTransactions;
    
    @ManyToMany(mappedBy = "deals", fetch = FetchType.LAZY)
    private List<Tag> tags;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Category category;
    
    @ManyToMany(mappedBy = "favDeals", fetch = FetchType.LAZY)
    private List<Customer> favCustomers;
    
    @ManyToMany(mappedBy = "deals", fetch = FetchType.LAZY)
    private List<Customer> customers;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Business business;
    
    @OneToMany(mappedBy = "deal", fetch = FetchType.LAZY)
    private List<Review> reviews;
    
    public Deal() {
        saleTransactions = new ArrayList<>();
        tags = new ArrayList<>();
        favCustomers = new ArrayList<>();
        customers = new ArrayList<>();
        reviews = new ArrayList<>();
        enabled = true;
        redeemed = false;
    }

    public Deal(String serialNum, String dealName, String description, Date startDateTime, Date endDateTime, Integer quantityLeft, BigDecimal unitPrice) {
        this();
        this.serialNum = serialNum;
        this.dealName = dealName;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.quantityLeft = quantityLeft;
        this.unitPrice = unitPrice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dealId != null ? dealId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the dealId fields are not set
        if (!(object instanceof Deal)) {
            return false;
        }
        Deal other = (Deal) object;
        if ((this.dealId == null && other.dealId != null) || (this.dealId != null && !this.dealId.equals(other.dealId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Deal[ id=" + dealId + " ]";
    }
    
    public Long getDealId() {
        return dealId;
    }

    public void setDealId(Long dealId) {
        this.dealId = dealId;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Integer getQuantityLeft() {
        return quantityLeft;
    }

    public void setQuantityLeft(Integer quantityLeft) {
        this.quantityLeft = quantityLeft;
    }

    public List<SaleTransaction> getSaleTransactions() {
        return saleTransactions;
    }

    public void setSaleTransactions(List<SaleTransaction> saleTransactions) {
        this.saleTransactions = saleTransactions;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Customer> getFavCustomers() {
        return favCustomers;
    }

    public void setFavCustomers(List<Customer> favCustomers) {
        this.favCustomers = favCustomers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    
    public void addTag(Tag tag)
    {
        if(tag != null)
        {
            if(!this.tags.contains(tag))
            {
                this.tags.add(tag);
                
                if(!tag.getDeals().contains(this))
                {                    
                    tag.getDeals().add(this);
                }
            }
        }
    }
       
    public void removeTag(Tag tag)
    {
        if(tag != null)
        {
            if(this.tags.contains(tag))
            {
                this.tags.remove(tag);
                
                if(tag.getDeals().contains(this))
                {
                    tag.getDeals().remove(this);
                }
            }
        }
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isRedeemed() {
        return redeemed;
    }

    public void setRedeemed(boolean redeemed) {
        this.redeemed = redeemed;
    }
    
    
    
}
