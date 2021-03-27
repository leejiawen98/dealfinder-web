/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.DealSessionBeanLocal;
import entity.Deal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leejiawen98
 */
@Named(value = "viewAllBusinessListingsManagedBean")
@ViewScoped
public class ViewAllBusinessListingsManagedBean implements Serializable {

    @EJB
    private DealSessionBeanLocal dealSessionBean;

    private List<Deal> deals;
    
    private Deal selectedDeal;
    
    @PostConstruct
    public void postConstruct()
    {
        deals = dealSessionBean.retrieveAllDeals();
    }
    
    public ViewAllBusinessListingsManagedBean() {
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
    
}
