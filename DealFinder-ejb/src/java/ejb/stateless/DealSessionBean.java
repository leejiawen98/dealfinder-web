/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Deal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewDealException;
import util.exception.DealNotFoundException;
import util.exception.DealQtyInsufficientException;

/**
 *
 * @author yeerouhew
 */
@Stateless
public class DealSessionBean implements DealSessionBeanLocal {

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    //create
//    public Deal createNewDeal(Deal newDeal, Long categoryId, List<Long> tagIds){
//        try{
//            if(categoryId == null){
//                throw new CreateNewDealException("The new product must be associated to a leaf category");
//            }
//            
//            //need category session 
//            
//        }
//    }
    
    
    
    @Override
    public List<Deal> retrieveAllDeals() {
        Query query = em.createQuery("SELECT d FROM Deal d ORDER BY d.serialNum ASC");
        List<Deal> deals = query.getResultList();

        for (Deal deal : deals) {
            deal.getCategory();
            deal.getTags().size();
        }

        return deals;
    }

    @Override
    public List<Deal> searchDealByName(String searchString) {
        Query query = em.createQuery("SELECT d FROM Deal d WHERE d.dealName LIKE :inSearchString ORDER BY d.serialNum ASC");
        query.setParameter("inSearchString", searchString);

        List<Deal> deals = query.getResultList();

        for (Deal deal : deals) {
            deal.getCategory();
            deal.getTags().size();
        }

        return deals;
    }

    //filter by category
//    public List<Deal> filterDealByCategory(Long categoryId){
//        List<Deal> deals = new ArrayList<>();
//        Category category = 
//        
//    }
    
    
    
    
    //filter by tags
    
    
    
    
    
    @Override
    public Deal getDealByDealId(Long dealId) throws DealNotFoundException {
        Deal deal = em.find(Deal.class, dealId);

        if (deal != null) {
            deal.getCategory();
            deal.getTags().size();

            return deal;
        } else {
            throw new DealNotFoundException("Deal Id " + dealId + " does not exist!");
        }
    }

    @Override
    public Deal getDealByDealSerialNum(String serialNum) throws DealNotFoundException {
        Query query = em.createQuery("SELECT d FROM Deal d WHERE d.serialNum = :inSerialNum");
        query.setParameter("inSerialNum", serialNum);

        try {
            Deal deal = (Deal) query.getSingleResult();
            deal.getCategory();
            deal.getTags().size();

            return deal;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new DealNotFoundException("Serial Number " + serialNum + " does not exist!");
        }
    }

    //update
    

    //delete
    
    
    //debit qty on hand
    @Override
    public void debitQtyOnHand(Long dealId, Integer qtyToDebit) throws DealQtyInsufficientException, DealNotFoundException {
        Deal deal = getDealByDealId(dealId);

        if (deal.getQuantityLeft() >= qtyToDebit) {
            deal.setQuantityLeft(deal.getQuantityLeft() - qtyToDebit);
        } else {
            throw new DealQtyInsufficientException("Deal " + deal.getSerialNum() + " quantity left is " + deal.getQuantityLeft() + " :not sufficient");
        }
    }

    //credit qty on hand
    @Override
    public void creditQtyOnHand(Long dealId, Integer qtyToCredit) throws DealNotFoundException{
        Deal deal = getDealByDealId(dealId);
        deal.setQuantityLeft(deal.getQuantityLeft() + qtyToCredit);
    }
   
    
    
    //add subcatgory to deal
    
    
}
