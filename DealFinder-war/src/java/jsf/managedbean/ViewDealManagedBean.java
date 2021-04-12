/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Deal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.sql.DataSource;

/**
 *
 * @author yeerouhew
 */
@Named(value = "viewDealManagedBean")
@ViewScoped
public class ViewDealManagedBean implements Serializable {

    @Resource(name = "dealFinder")
    private DataSource dealFinder;
    
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
