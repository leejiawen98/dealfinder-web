/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.SaleTransactionSessionBeanLocal;
import entity.Business;
import entity.SaleTransaction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.enumeration.MonthEnum;
import util.exception.BusinessNotFoundException;

/**
 *
 * @author leejiawen98
 */
@Named(value = "salesReportManagedBean")
@ViewScoped
public class SalesReportManagedBean implements Serializable{

    @EJB
    private SaleTransactionSessionBeanLocal saleTransactionSessionBean;

    private List<SaleTransaction> sales;
    
    private List<String> month = new ArrayList<>();
    
    private String selectedMonth;
    
    private Business business;

    public SalesReportManagedBean() {
        
    }
    
    @PostConstruct
    public void postConstruct()
    {
        business = (Business) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        Calendar cal = Calendar.getInstance();
        int mth = cal.getTime().getMonth();
        int i = 0;
        for (MonthEnum m : MonthEnum.values())
        {
            if (mth == i)
                selectedMonth = m.toString();
            month.add(m.toString());
            i++;
        }
        changeMonth();
    }
    
    public void changeMonth()
    {
        try
        {
            sales = saleTransactionSessionBean.retrieveSaleTransactionDealByBusinessAndMonth(business.getId(), selectedMonth);
            Collections.sort(sales, new Comparator<SaleTransaction>() {
                @Override
                public int compare(SaleTransaction abc1, SaleTransaction abc2) {
                    return abc2.getTransactionDateTime().compareTo(abc1.getTransactionDateTime());
                }
            });
        }
        catch (BusinessNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }

    }
    
    public List<SaleTransaction> getSales() {
        return sales;
    }

    public void setSales(List<SaleTransaction> sales) {
        this.sales = sales;
    }

    public List<String> getMonth() {
        return month;
    }

    public void setMonth(List<String> month) {
        this.month = month;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

}
