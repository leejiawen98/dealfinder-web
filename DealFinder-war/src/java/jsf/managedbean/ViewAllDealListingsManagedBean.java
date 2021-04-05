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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import util.exception.CategoryNotFoundException;
import util.exception.DealNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;
import util.exception.UpdateDealException;

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

    //update
    private Deal dealToUpdate;
    private Long categoryIdToUpdate;
    private List<Long> tagIdToUpdate;

    //delete/disable
    private Deal dealToDelete;
    private String reason;

    public ViewAllDealListingsManagedBean() {

    }

    @PostConstruct
    public void postConstruct() {
        Business business = (Business) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        deals = dealSessionBeanLocal.retrieveDealByBusinessId(business.getId());
        categories = categorySessionBeanLocal.retrieveAllLeafCategories();
        tags = tagSessionBeanLocal.retrieveAllTags();

    }

    public void doUpdateDeal(ActionEvent event) {
        dealToUpdate = (Deal) event.getComponent().getAttributes().get("dealToUpdate");
        categoryIdToUpdate = dealToUpdate.getCategory().getCategoryId();
        tagIdToUpdate = new ArrayList<>();

        for (Tag tag : dealToUpdate.getTags()) {
            tagIdToUpdate.add(tag.getTagId());
        }
    }

    public void updateDeal(ActionEvent event) {
        if (categoryIdToUpdate == 0) {
            categoryIdToUpdate = null;
        }

        try {
            dealToUpdate.setEnabled(true);
            dealSessionBeanLocal.updateDeal(dealToUpdate, categoryIdToUpdate, tagIdToUpdate);

            for (Category c : categories) {
                if (c.getCategoryId().equals(categoryIdToUpdate)) {
                    dealToUpdate.setCategory(c);
                    break;
                }
            }

            dealToUpdate.getTags().clear();
            for (Tag t : tags) {
                if (tagIdToUpdate.contains(t)) {
                    dealToUpdate.getTags().add(t);
                }
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Deal updated successfully", null));

        } catch (DealNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deal does not exist", null));

        } catch (InputDataValidationException | UpdateDealException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating the deal: " + ex.getMessage(), null));

        } catch (TagNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tag does not exist", null));

        } catch (CategoryNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Category does not exist", null));
        }
    }

    public void doDisable(ActionEvent event) {
        dealToDelete = (Deal) event.getComponent().getAttributes().get("dealToDelete");
        try {
            dealSessionBeanLocal.deleteDeal(dealToDelete.getDealId());
            deals.remove(dealToDelete);

            if (filteredDeals != null) {
                filteredDeals.remove(dealToDelete);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product deleted successfully", null));
        } catch (DealNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deal cannot be found", null));
        }

    }

    public void disable() {
        if (!reason.isEmpty()) {
            try {
                if (dealToDelete.isEnabled()) {
                    dealToDelete.setEnabled(false);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Deal is disabled", null));
                } else {
                    dealToDelete.setEnabled(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Deal is enabled", null));
                }
                dealSessionBeanLocal.updateDealStatus(dealToDelete);

                // send email
                setReason(null);
            } catch (DealNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please input a justification", null));
        }
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

    public Deal getDealToUpdate() {
        return dealToUpdate;
    }

    public void setDealToUpdate(Deal dealToUpdate) {
        this.dealToUpdate = dealToUpdate;
    }

    public Long getCategoryIdToUpdate() {
        return categoryIdToUpdate;
    }

    public void setCategoryIdToUpdate(Long categoryIdToUpdate) {
        this.categoryIdToUpdate = categoryIdToUpdate;
    }

    public List<Long> getTagIdToUpdate() {
        return tagIdToUpdate;
    }

    public void setTagIdToUpdate(List<Long> tagIdToUpdate) {
        this.tagIdToUpdate = tagIdToUpdate;
    }

    public Deal getDealToDelete() {
        return dealToDelete;
    }

    public void setDealToDelete(Deal dealToDelete) {
        this.dealToDelete = dealToDelete;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
