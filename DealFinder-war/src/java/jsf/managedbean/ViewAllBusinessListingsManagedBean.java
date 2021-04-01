/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.DealSessionBeanLocal;
import ejb.session.stateless.TagSessionBeanLocal;
import entity.Deal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.DealNotFoundException;

/**
 *
 * @author leejiawen98
 */
@Named(value = "viewAllBusinessListingsManagedBean")
@ViewScoped
public class ViewAllBusinessListingsManagedBean implements Serializable {

    @EJB
    private TagSessionBeanLocal tagSessionBean;

    @EJB
    private DealSessionBeanLocal dealSessionBean;
    
    private List<Deal> deals;
    
    private Deal selectedDeal;
    
    private String reason;
    
    @PostConstruct
    public void postConstruct()
    {
        deals = dealSessionBean.retrieveAllDeals();
    }
    
    public ViewAllBusinessListingsManagedBean() {
    }

    public void disable()
    {
        if (!reason.isEmpty())
        {
            try{
                if (selectedDeal.isEnabled())
                {
                    selectedDeal.setEnabled(false);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Deal is disabled", null));
                }
                else
                {
                    selectedDeal.setEnabled(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Deal is enabled", null));
                }
                dealSessionBean.updateDealStatus(selectedDeal);

                // send email
                reason = null;
            }
            catch (DealNotFoundException ex)
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            }
        }
        else
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please input a justification", null));
        }
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

    public Deal getSelectedDeal() {
        return selectedDeal;
    }

    public void setSelectedDeal(Deal selectedDeal) {
        this.selectedDeal = selectedDeal;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
}
