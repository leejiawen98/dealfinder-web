/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminSessionBeanLocal;
import ejb.session.stateless.BusinessSessionBeanLocal;
import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.DealSessionBeanLocal;
import ejb.session.stateless.TagSessionBeanLocal;
import entity.Admin;
import entity.Business;
import entity.Category;
import entity.Deal;
import entity.Tag;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.BusinessNotFoundException;
import util.exception.BusinessUsernameExistException;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.CreateNewDealException;
import util.exception.CreateNewTagException;
import util.exception.DealSerialNumberExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author leejiawen98
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    //-------can remove 
    @EJB
    private TagSessionBeanLocal tagSessionBean;

    @EJB
    private CategorySessionBeanLocal categorySessionBean;

    @EJB
    private DealSessionBeanLocal dealSessionBean;
    
    @EJB
    private BusinessSessionBeanLocal businessSessionBean;
    // ------

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
        
        try
        {
            Long b1 = businessSessionBean.createBusiness(new Business("Zalora", "zalora", "password", "leejiawen98@gmail.com", "97716383", "Jurong East"));
            Long b2 = businessSessionBean.createBusiness(new Business("Obba Jjajang", "obba", "password", "e0417580@u.nus.edu", "87364523", "Tanjong Pagar"));
            Long b3 = businessSessionBean.createBusiness(new Business("NTUC", "ntuc", "password", "leejiawen98@hotmail.sg", "98373643", "Hougang"));
            
            Category c1 = categorySessionBean.createNewCategoryEntity(new Category("Fashion", "Clothes and shoes"), null);
            Category c2 = categorySessionBean.createNewCategoryEntity(new Category("Luxury Brands", "Designers goods"), 1l);
            
            Category c3 = categorySessionBean.createNewCategoryEntity(new Category("Food", "Food vouchers and deals"), null);
            Category c4 = categorySessionBean.createNewCategoryEntity(new Category("Korean", "Authentic Korean Restaurants"), 4l);
            
            Category c5 = categorySessionBean.createNewCategoryEntity(new Category("Grocery", "Grocery deals"), null);
            Category c6 = categorySessionBean.createNewCategoryEntity(new Category("Instant Noodles", "Instant noodle bundles"), 6l);
            
            Tag t1 = tagSessionBean.createNewTagEntity(new Tag("Popular"));
            Tag t2 = tagSessionBean.createNewTagEntity(new Tag("Limited"));
            Tag t3 = tagSessionBean.createNewTagEntity(new Tag("New"));
            
            List<Long> tags1 = new ArrayList<>();
            tags1.add(t1.getTagId());
            tags1.add(t2.getTagId());
            
            List<Long> tags2 = new ArrayList<>();
            tags2.add(t2.getTagId());
            tags2.add(t3.getTagId());
            
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, +2);
            Date result = cal.getTime();
            cal.add(Calendar.MONTH, +5);
            Date result2 = cal.getTime();
            cal.add(Calendar.MONTH, +7);
            Date result3 = cal.getTime();
            
            dealSessionBean.createNewDeal(new Deal("Z001", "Converse Sneakers", "Chuck Taylor All Star", new Date(), result, 100, BigDecimal.valueOf(45)), c1.getCategoryId(), tags1, b1);
            dealSessionBean.createNewDeal(new Deal("K001", "Obba Jjajang Deal Set", "2 person set meal of your jjajangmyeon! Original @ $15 each", new Date(), result2, 50, BigDecimal.valueOf(20)), c4.getCategoryId(), tags1, b2);
            dealSessionBean.createNewDeal(new Deal("N001", "Koka Instant Noodle Packet (6's)", "Spicy Singaporean Fried Noodles", new Date(), result3, 300, BigDecimal.valueOf(3)), c6.getCategoryId(), tags2, b3);
           
        }
        catch (BusinessUsernameExistException | UnknownPersistenceException | CreateNewCategoryException | InputDataValidationException | CreateNewTagException | BusinessNotFoundException | CategoryNotFoundException | CreateNewDealException | DealSerialNumberExistException ex)
        {
            
        }
        
        
    }
}
