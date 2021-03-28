/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminSessionBeanLocal;
import ejb.session.stateless.BusinessSessionBeanLocal;
import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.TagSessionBean;
import ejb.session.stateless.TagSessionBeanLocal;
import entity.Admin;
import entity.Business;
import entity.Category;
import entity.Tag;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.BusinessUsernameExistException;
import util.exception.CreateNewCategoryException;
import util.exception.CreateNewTagException;
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

    @EJB
    private TagSessionBeanLocal tagSessionBean;
    @EJB
    private CategorySessionBeanLocal categorySessionBean;
    @EJB
    private BusinessSessionBeanLocal businessSessionBean;
    @EJB
    private AdminSessionBeanLocal adminSessionBean;

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    public DataInitializationSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        List<Admin> admins = adminSessionBean.getAllAdmins();
        if (admins.isEmpty()) {
            initialiseAdmins();
            initialiseBusiness();
            initialiseCategoryTags();
        }
    }

    public void initialiseAdmins() {
        adminSessionBean.createAdmin(new Admin("admin1", "password", "admin", "1"));
        adminSessionBean.createAdmin(new Admin("admin2", "password", "admin", "2"));
        adminSessionBean.createAdmin(new Admin("admin3", "password", "admin", "3"));
    }

    public void initialiseBusiness() {
        try {
            businessSessionBean.createBusiness(new Business("Business 1", "biz1", "password", "business1@gmail.com", "88888888", "biz1"));
        } catch (BusinessUsernameExistException | UnknownPersistenceException | InputDataValidationException ex) {
            ex.printStackTrace();
        }
    }

    public void initialiseCategoryTags() {
        try {
            Category catFnB = categorySessionBean.createNewCategoryEntity(new Category("F&B", "F&B"), null);
            Category catBeauty = categorySessionBean.createNewCategoryEntity(new Category("Beauty", "Beauty"), null);
            Category catRetail = categorySessionBean.createNewCategoryEntity(new Category("Retail", "Retail"), null);
            Category catLeisure = categorySessionBean.createNewCategoryEntity(new Category("Leisure", "Leisure"), null);
            Category catFitness = categorySessionBean.createNewCategoryEntity(new Category("Fitness", "Fitness"), null);
            
            //F&B
            Category subCatChinese = categorySessionBean.createNewCategoryEntity(new Category("Chinese Cuisine", "Chinese Cuisine"), catFnB.getCategoryId());
            Category subCatMuslim = categorySessionBean.createNewCategoryEntity(new Category("Muslim Cuisine", "Muslim Cuisine"), catFnB.getCategoryId());
            Category subCatDessert = categorySessionBean.createNewCategoryEntity(new Category("Dessert", "Dessert"), catFnB.getCategoryId());
            //Beauty
            Category subCatFacial = categorySessionBean.createNewCategoryEntity(new Category("Facial", "Facial"), catBeauty.getCategoryId());
            Category subCatBrows = categorySessionBean.createNewCategoryEntity(new Category("Brows & Lashes", "Brows & Lashes"), catBeauty.getCategoryId());
            //Retail
            Category subCatShopping = categorySessionBean.createNewCategoryEntity(new Category("Shopping", "Shopping"), catRetail.getCategoryId());
            //Leisure
            Category subCatMovie = categorySessionBean.createNewCategoryEntity(new Category("Movies", "Movies"), catLeisure.getCategoryId());
            Category subCatAttraction = categorySessionBean.createNewCategoryEntity(new Category("Attractions", "Attractions"), catLeisure.getCategoryId());
            //Fitness
            Category subCatGym = categorySessionBean.createNewCategoryEntity(new Category("Gym", "Gym"), catFitness.getCategoryId());
            
            Tag tagEntityPopular = tagSessionBean.createNewTagEntity(new Tag("popular"));
            Tag tagEntityDiscount = tagSessionBean.createNewTagEntity(new Tag("discount"));
            Tag tagEntityNew = tagSessionBean.createNewTagEntity(new Tag("new"));
            Tag tagEntityOneForOne = tagSessionBean.createNewTagEntity(new Tag("1-For-1"));

        } catch (InputDataValidationException | CreateNewCategoryException ex) {
            ex.printStackTrace();
        } catch (CreateNewTagException ex) {
            ex.printStackTrace();
        }
    }
}
