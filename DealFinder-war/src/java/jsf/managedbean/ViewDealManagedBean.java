/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ReviewSessionBeanLocal;
import entity.Deal;
import entity.Review;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import util.exception.DealNotFoundException;

/**
 *
 * @author yeerouhew
 */
@Named(value = "viewDealManagedBean")
@ViewScoped
public class ViewDealManagedBean implements Serializable {

    @EJB(name = "ReviewSessionBeanLocal")
    private ReviewSessionBeanLocal reviewSessionBeanLocal;

 
    private Deal dealToView;
    private Deal selectedDeal;
    
    public ViewDealManagedBean() {
        dealToView = new Deal();
    }
    
    public Deal getDealToView() {
        return dealToView;
    }
    
    public void setDealToView(Deal dealToView){
        this.dealToView = dealToView;
    }

    public Deal getSelectedDeal(){
        return selectedDeal;
    }
    
    public void setSelectedDeal(Deal selectedDeal){
        this.selectedDeal = selectedDeal;
    }

    
    
}
