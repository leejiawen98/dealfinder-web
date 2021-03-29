/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.BusinessSessionBeanLocal;
import entity.Business;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.email.EmailManager;
import util.exception.BusinessNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateBusinessException;

/**
 *
 * @author leejiawen98
 */
@Named(value = "verifyAccountsManagedBean")
@ViewScoped
public class VerifyAccountsManagedBean implements Serializable{

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
                    sendEmail(selectedBusiness.getEmail(), emailBody);
                    businessSessionBean.updateBusiness(selectedBusiness);
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
    }
    
    public void delete()
    {
        try
        {
            if (!selectedBusiness.getDeals().isEmpty())
            {
                businessSessionBean.deleteBusiness(selectedBusiness.getId());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account Deleted", null));
            }
            else
            {
                try 
                {
                    selectedBusiness.setVerified(false);
                    businessSessionBean.updateBusiness(selectedBusiness);
                    String emailBody = "Your business account '" + selectedBusiness.getUsername() + "' has been rejected/disabled due to '" + rejectReason + "'. Please check with the administrator of DealFinder for further information";
                    sendEmail(selectedBusiness.getEmail(), emailBody);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unable to delete because it has existing deals, account is disabled", null));
                }
                catch (BusinessNotFoundException | InputDataValidationException | UpdateBusinessException ex)
                {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
                }
            }
        }
        catch (BusinessNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
        }
    }
    
    public void sendEmail(String receipientEmail, String emailBody)
    {
        EmailManager emailManager = new EmailManager("leejiawen98@gmail.com", "Endeline1234.");
        Boolean result = emailManager.email("leejiawen98@gmail.com", "leejiawen98@gmail.com", emailBody);
//        Boolean result = true;
        if(result)
        {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Email sent successfully", null));
        }
        else
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while sending email", null));
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
