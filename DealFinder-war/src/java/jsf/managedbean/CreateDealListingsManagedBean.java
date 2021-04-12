/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.DealSessionBeanLocal;
import ejb.session.stateless.TagSessionBeanLocal;
import entity.Business;
import entity.Category;
import entity.Deal;
import entity.Tag;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.primefaces.model.StreamedContent;
import util.exception.BusinessNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewDealException;
import util.exception.DealSerialNumberExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yeerouhew
 */
@Named(value = "createDealListingsManagedBean")
@RequestScoped
public class CreateDealListingsManagedBean {

    @EJB
    private TagSessionBeanLocal tagSessionBeanLocal;
    @EJB
    private CategorySessionBeanLocal categorySessionBeanLocal;
    @EJB
    private DealSessionBeanLocal dealSessionBeanLocal;
    
    private Deal newDeal;
    private Long newCategoryId;
    private List<Long> newTagIds;
    
    private List<Tag> tagList;
    private List<Category> categoryList;
    
    //for qr
//    private StreamedContent QR;
//    private ByteArrayOutputStream rawQR;

    public CreateDealListingsManagedBean() {
        newDeal = new Deal();
    }
    
    @PostConstruct
    public void postConstruct(){
        categoryList = categorySessionBeanLocal.retrieveAllLeafCategories();
        tagList = tagSessionBeanLocal.retrieveAllTags();
    }
    
    public void createNewDealListing() throws IOException{
        if(newCategoryId == 0){
            newCategoryId = null;
        }
        
        Business business = (Business)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        
        //generate qr code
//        rawQR = QRCode.from(business.getId().toString()).to(ImageType.PNG).stream();
//        ByteArrayInputStream is = new ByteArrayInputStream(rawQR.toByteArray());
//        byte[] qrImg = new byte[is.available()];
//        is.read(qrImg);
        
        try {
            Deal deal = dealSessionBeanLocal.createNewDeal(newDeal, newCategoryId, newTagIds, business.getId());
            
            newDeal = new Deal();
            newCategoryId = null;
            newTagIds = new ArrayList<>();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New deal listing created successfully (Deal Id): " + deal.getDealId(), null));
            
        } catch (CreateNewDealException | UnknownPersistenceException | InputDataValidationException | BusinessNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while creating new deal: " + ex.getMessage(), null));
        } catch (CategoryNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while creating new deal. Category not found:  " + ex.getMessage(), null));
        } catch (DealSerialNumberExistException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while creating new deal. Serial Number exist:  " + ex.getMessage(), null));
        }
    }

    public Deal getNewDeal() {
        return newDeal;
    }

    public void setNewDeal(Deal newDeal) {
        this.newDeal = newDeal;
    }

    public Long getNewCategoryId() {
        return newCategoryId;
    }

    public void setNewCategoryId(Long newCategoryId) {
        this.newCategoryId = newCategoryId;
    }

    public List<Long> getNewTagIds() {
        return newTagIds;
    }

    public void setNewTagIds(List<Long> newTagIds) {
        this.newTagIds = newTagIds;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
    
    
    
}
