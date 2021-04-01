/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.SaleTransactionSessionBeanLocal;
import entity.Business;
import entity.SaleTransaction;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import util.enumeration.MonthEnum;
import util.exception.BusinessNotFoundException;
import java.text.SimpleDateFormat;

/**
 *
 * @author leejiawen98
 */
@Named(value = "salesReportManagedBean")
@ViewScoped
public class SalesReportManagedBean implements Serializable{

    @Resource(name = "dealFinder")
    private DataSource dealFinder;

    @EJB
    private SaleTransactionSessionBeanLocal saleTransactionSessionBean;

    private List<SaleTransaction> sales;
    
    private List<String> month = new ArrayList<>();
    
    private String selectedMonth;
    
    private Business business;
    
    private BigDecimal totalAmt;
   

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
            getTotalAmtInSelectedMonth();
        }
        catch (BusinessNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }
    
    public void getTotalAmtInSelectedMonth()
    {
        totalAmt = BigDecimal.ZERO;
        for (SaleTransaction s: sales)
        {
            totalAmt = totalAmt.add(s.getTotalAmount());
        }
    }
    
    public void generateReport(ActionEvent event) {
        if (sales.isEmpty())
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No sales for this month", null));
        }
            else
            {
            List<Long> st = new ArrayList<>();
            for (SaleTransaction s: sales)
            {
                st.add(s.getSaleTransactionId());
            }
            try {
                HashMap parameters = new HashMap();
                parameters.put("stIds", st);
                parameters.put("totalAmt", totalAmt.toString());
                parameters.put("businessName", business.getName());
                parameters.put("businessAddress", business.getAddress());
                parameters.put("month", selectedMonth);
                String relativePath = "./resources/images/";
                parameters.put("CONTEXT",FacesContext.getCurrentInstance().getExternalContext().getRealPath(relativePath) + "/logo.jpeg");

                InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreport/salesReportMonth.jasper");
                OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();

                JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, dealFinder.getConnection());
            } catch (JRException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
            }
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

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

}
