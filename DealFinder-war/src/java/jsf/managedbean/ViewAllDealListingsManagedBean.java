/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.DealSessionBeanLocal;
import ejb.session.stateless.TagSessionBeanLocal;
import entity.Business;
import entity.Category;
import entity.Deal;
import entity.Tag;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author yeerouhew
 */
@Named(value = "viewAllDealListingsManagedBean")
@ViewScoped
public class ViewAllDealListingsManagedBean implements Serializable {

    @EJB
    private TagSessionBeanLocal tagSessionBeanLocal;
    @EJB
    private CategorySessionBeanLocal categorySessionBeanLocal;
    @EJB
    private DealSessionBeanLocal dealSessionBeanLocal;
    
    @Inject
    private ViewDealManagedBean viewDealManagedBean;
    
    private List<Deal> deals;
    private List<Deal> filteredDeals;
    
    private List<Category> categories;
    private List<Tag> tags;
    
    
    public ViewAllDealListingsManagedBean() {
        
    }
    
    @PostConstruct
    public void postConstruct(){
        Business business = (Business)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        deals = dealSessionBeanLocal.retrieveDealByBusinessId(business.getId());
        categories = categorySessionBeanLocal.retrieveAllLeafCategories();
        tags = tagSessionBeanLocal.retrieveAllTags();
        
    }
    
    public void viewDealDetails(ActionEvent event){
        Deal deal = (Deal)event.getComponent().getAttributes().get("deal123");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("dealToView", deal);
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

    public List<Deal> getFilteredDeals() {
        return filteredDeals;
    }

    public void setFilteredDeals(List<Deal> filteredDeals) {
        this.filteredDeals = filteredDeals;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public ViewDealManagedBean getViewDealManagedBean() {
        return viewDealManagedBean;
    }

    public void setViewDealManagedBean(ViewDealManagedBean viewDealManagedBean) {
        this.viewDealManagedBean = viewDealManagedBean;
    }
    
}
