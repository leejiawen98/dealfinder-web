/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.BusinessSessionBeanLocal;
import entity.Business;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import util.exception.BusinessUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author leejiawen98
 */
@Named(value = "registerManagedBean")
@RequestScoped
public class RegisterManagedBean {

    @EJB
    private BusinessSessionBeanLocal businessSessionBean;
    
    private String username;
    private String password;
    private String email;
    private String name;
    private String mobileNum;
    private String address;
    
    public RegisterManagedBean() {
        
    }
    
    public void register() throws IOException
    {
        try
        {
            Business business = new Business(name, username, password, email, mobileNum, address);
            businessSessionBean.createBusiness(business);
            FacesContext.getCurrentInstance().addMessage("registersucccess", new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration submitted", "Please await for account verification"));
            
            username = "";
            password = "";
            email = "";
            name = "";
            mobileNum = "";
            address = "";
        }
        catch(BusinessUsernameExistException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Business already registered, " + ex.getMessage(), null));
        }
        catch(InputDataValidationException | UnknownPersistenceException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Input not valid! " + ex.getMessage(), null));
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    
}
