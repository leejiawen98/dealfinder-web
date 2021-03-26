/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminSessionBeanLocal;
import entity.Admin;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author leejiawen98
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private AdminSessionBeanLocal adminSessionBean;

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;


    public DataInitializationSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        List<Admin> admins = adminSessionBean.getAllAdmins();
        if (admins.isEmpty())
        {
            initialiseAdmins();
        }
    }
    
    public void initialiseAdmins()
    {
        adminSessionBean.createAdmin(new Admin("admin1", "password", "admin", "1"));
        adminSessionBean.createAdmin(new Admin("admin2", "password", "admin", "2"));
        adminSessionBean.createAdmin(new Admin("admin3", "password", "admin", "3"));
        
    }
}
