/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.BusinessSessionBeanLocal;
import ejb.session.stateless.EmailSessionBeanLocal;
import entity.Business;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.email.EmailManager;
import util.exception.BusinessNotFoundException;
import util.exception.EmailException;
import util.exception.InputDataValidationException;
import util.exception.UpdateBusinessException;
import util.thread.RunnableNotification;

/**
 *
 * @author leejiawen98
 */
@Named(value = "verifyAccountsManagedBean")
@ViewScoped
public class VerifyAccountsManagedBean implements Serializable{

    @EJB
    private EmailSessionBeanLocal emailSessionBean;

    @EJB
    private BusinessSessionBeanLocal businessSessionBean;

    private List<Business> businessAcc;
    
    private Business selectedBusiness;
    
    private Integer selectedStatus;
    
    private String rejectReason;
    
    
    public VerifyAccountsManagedBean() {
        selectedBusiness = null;
    }
    
    @PostConstruct
    public void postConstruct()
    {
        businessAcc = businessSessionBean.getAllBusinesses();
        Collections.sort(businessAcc, new Comparator<Business>() {
            @Override
            public int compare(Business abc1, Business abc2) {
                return Boolean.compare(abc1.getVerified(),abc2.getVerified());
            }
        });
    }

    public void verify() throws IOException
    {
        String emailBody = "";
        if (selectedStatus == 1 && !selectedBusiness.getVerified())
        {
            selectedBusiness.setVerified(true);   
            emailBody = "Your business account '" + selectedBusiness.getUsername() + "' has been verified! Head down to DealFinder to start posting listings!";
        }
        else if (selectedStatus == 2 && selectedBusiness.getVerified())
        {
            selectedBusiness.setVerified(false);
            emailBody = "Your business account '" + selectedBusiness.getUsername() + "' has been rejected/disabled due to '" + rejectReason + "'. Please check with the administrator of DealFinder for further information";
        }
            try
            {
                if (emailBody != "")
                {
                    businessSessionBean.updateBusiness(selectedBusiness);
                    sendEmail(emailBody); 
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Update done and email sent to user", null));
                }
                else
                {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No updates made", null));
                }
            }
            catch (BusinessNotFoundException | InputDataValidationException | UpdateBusinessException ex)
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            }
            rejectReason = "";
    }
    
    public void delete()
    {
        try
        {
            if (selectedBusiness.getDeals().isEmpty())
            {
                businessSessionBean.deleteBusiness(selectedBusiness.getId());
                String emailBody = "Your business account '" + selectedBusiness.getUsername() + "' has been deleted. Please check with the administrator of DealFinder for further information";
                sendEmail(emailBody);                         
                businessAcc.remove(selectedBusiness);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account Deleted", null));
            }
            else
            {
                selectedBusiness.setVerified(false);
                businessSessionBean.updateBusiness(selectedBusiness);
                String emailBody = "Your business account '" + selectedBusiness.getUsername() + "' has been un-verified. Please check with the administrator of DealFinder for further information";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unable to delete because it has existing deals, account is un-verified", null));    
                sendEmail(emailBody);  
            }
        }
        catch (BusinessNotFoundException | InputDataValidationException | UpdateBusinessException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
        }
        rejectReason = "";
    }
    
    public void sendEmail(String emailBody)
    {
        try
        {
            Future<Boolean> asyncResult = emailSessionBean.emailBusinessVerification(selectedBusiness, emailBody);
            RunnableNotification rn = new RunnableNotification(asyncResult);
            rn.start(); 
        }
        catch (EmailException | InterruptedException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
        }
    }
    
    public List<Business> getBusinessAcc() {
        return businessAcc;
    }

    public void setBusinessAcc(List<Business> businessAcc) {
        this.businessAcc = businessAcc;
    }

    public Business getSelectedBusiness() {
        return selectedBusiness;
    }

    public void setSelectedBusiness(Business selectedBusiness) {
        this.selectedBusiness = selectedBusiness;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Integer getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(Integer selectedStatus) {
        this.selectedStatus = selectedStatus;
    }
    
}
