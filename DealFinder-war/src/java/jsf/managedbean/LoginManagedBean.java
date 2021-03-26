/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AdminSessionBeanLocal;
import ejb.session.stateless.BusinessSessionBeanLocal;
import entity.Admin;
import entity.Business;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author leejiawen98
 */
@Named(value = "loginManagedBean")
@RequestScoped
public class LoginManagedBean {

    @EJB
    private AdminSessionBeanLocal adminSessionBean;

    @EJB
    private BusinessSessionBeanLocal businessSessionBean;

    
    private String accountType;
    private String username;
    private String password;
    
    
    public LoginManagedBean() {
        
    }

    public void login() throws IOException
    {
        try
        {
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            if (accountType.equals("Business"))
            {
                Business business = businessSessionBean.businessLogin(username, password);
                //check business verification
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", business);
            }
            else if (accountType.equals("Administrator"))
            {
                Admin admin = adminSessionBean.adminLogin(username, password);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", admin);
            }
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("accountType", accountType);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", null));
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/home.xhtml");
        }
        catch(InvalidLoginCredentialException ex)
        {
            ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage(), null));
        }
    }
    
    public void logout() throws IOException
    {
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
    }
    
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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
    
}
