/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminSessionBeanLocal;
import ejb.session.stateless.BusinessSessionBeanLocal;
import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.DealSessionBeanLocal;
import ejb.session.stateless.SaleTransactionSessionBeanLocal;
import ejb.session.stateless.TagSessionBeanLocal;
import entity.Admin;
import entity.Business;
import entity.Category;
import entity.Customer;
import entity.Deal;
import entity.SaleTransaction;
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
import util.exception.CreateNewSaleTransactionException;
import util.exception.CreateNewTagException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.DealNotFoundException;
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

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    @EJB
    private SaleTransactionSessionBeanLocal saleTransactionSessionBean;
    @EJB
    private DealSessionBeanLocal dealSessionBean;
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
            //initialiseCategoryTags();
            initialiseData();
            initialiseSales();
        }
    }

    public void initialiseAdmins() {
        adminSessionBean.createAdmin(new Admin("admin1", "password", "admin", "1"));
        adminSessionBean.createAdmin(new Admin("admin2", "password", "admin", "2"));
        adminSessionBean.createAdmin(new Admin("admin3", "password", "admin", "3"));
    }

    // yeerou
    public void initialiseBusiness() {

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
        
    // jiawen
    public void initialiseData() {
        try
        {
            Long b1 = businessSessionBean.createBusiness(new Business("Zalora", "zalora", "password", "leejiawen98@gmail.com", "97716383", "Jurong East"));
            Long b2 = businessSessionBean.createBusiness(new Business("Obba Jjajang", "obba", "password", "e0417580@u.nus.edu", "87364523", "Tanjong Pagar"));
            Long b3 = businessSessionBean.createBusiness(new Business("NTUC", "ntuc", "password", "leejiawen98@hotmail.sg", "98373643", "Hougang"));
            
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

            
            Tag t1 = tagSessionBean.createNewTagEntity(new Tag("limited"));
            Tag t2 = tagSessionBean.createNewTagEntity(new Tag("local"));
            Tag t3 = tagSessionBean.createNewTagEntity(new Tag("exclusive"));
            
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
            
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, +1);
            Date result4 = cal.getTime();
            
            Deal d1 = dealSessionBean.createNewDeal(new Deal("Z001", "Converse Sneakers", "Chuck Taylor All Star", new Date(), result, 100, BigDecimal.valueOf(45), 100), subCatShopping.getCategoryId(), tags1, b1);
            Deal d2 = dealSessionBean.createNewDeal(new Deal("K001", "Obba Jjajang Deal Set", "2 person set meal of your jjajangmyeon! Original @ $15 each", new Date(), result2, 50, BigDecimal.valueOf(20), 50), subCatChinese.getCategoryId(), tags1, b2);
            Deal d3 = dealSessionBean.createNewDeal(new Deal("N001", "Koka Instant Noodle Packet (6's)", "Spicy Singaporean Fried Noodles", new Date(), result3, 300, BigDecimal.valueOf(3), 300), subCatShopping.getCategoryId(), tags2, b3);
            
            Long cust1 = customerSessionBean.createCustomer(new Customer("Jia Wen", "Lee", BigDecimal.ZERO, "jiawen", "password", "leejiawen98@gmail.com", "97716383"));
            Long cust2 = customerSessionBean.createCustomer(new Customer("Stella", "Ang", BigDecimal.ZERO, "stella", "password", "stella98@gmail.com", "83746343"));
            Long cust3 = customerSessionBean.createCustomer(new Customer("Yeerou", "Hew", BigDecimal.ZERO, "yeerou", "password", "yeerou98@gmail.com", "85744394"));
            Long cust4 = customerSessionBean.createCustomer(new Customer("Aaron", "Tan", BigDecimal.ZERO, "aaron", "password", "aaron98@gmail.com", "93847463"));
            
        }
        catch (BusinessUsernameExistException | UnknownPersistenceException | CreateNewCategoryException | InputDataValidationException | 
                CreateNewTagException | BusinessNotFoundException | CategoryNotFoundException | CreateNewDealException | DealSerialNumberExistException 
                 | CustomerUsernameExistException ex)
        {
            
        }
    }
    
    public void initialiseSales()
    {
        Calendar cal = Calendar.getInstance();
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +1);
        Date result4 = cal.getTime();
        cal.add(Calendar.DATE, +3);
        Date result6 = cal.getTime();
        cal.add(Calendar.MONTH, +1);
        Date result5 = cal.getTime();
        
        try
        {
            Deal d1 = dealSessionBean.retrieveDealByDealId(1l);
            Deal d2 = dealSessionBean.retrieveDealByDealId(2l);
        
            saleTransactionSessionBean.createNewSaleTransaction(4l, new SaleTransaction(d1.getUnitPrice(), BigDecimal.valueOf(2).multiply(d1.getUnitPrice()), result4, 2), d1.getDealId());
            saleTransactionSessionBean.createNewSaleTransaction(4l, new SaleTransaction(d1.getUnitPrice(), BigDecimal.valueOf(5).multiply(d1.getUnitPrice()), result6, 5), d1.getDealId());
            saleTransactionSessionBean.createNewSaleTransaction(5l, new SaleTransaction(d2.getUnitPrice(), BigDecimal.valueOf(4).multiply(d1.getUnitPrice()),  result4, 4), d2.getDealId());
            
        }
        catch (CreateNewSaleTransactionException | CustomerNotFoundException | DealNotFoundException ex)
        {
            
        }
    }
}
