/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Deal;
import entity.Redemption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewRedemptionException;
import util.exception.CustomerNotFoundException;
import util.exception.DealNotFoundException;
import util.exception.RedemptionNotFoundException;

/**
 *
 * @author yeerouhew
 */
@Stateless
public class RedemptionSessionBean implements RedemptionSessionBeanLocal {

    @EJB
    private DealSessionBeanLocal dealSessionBeanLocal;
    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    @Override
    public Redemption createNewRedemption(Long customerId, Redemption newRedemption, Long dealId) throws CreateNewRedemptionException
    {
        if(newRedemption != null)
        {
            try
            {
                Customer customerEntity = customerSessionBeanLocal.getCustomerByCustomerId(customerId);
                newRedemption.setCustomer(customerEntity);
                customerEntity.getRedemptions().add(newRedemption);
                
                Deal deal = dealSessionBeanLocal.retrieveDealByDealId(dealId);
                newRedemption.setDeal(deal);
                deal.getRedemptions().add(newRedemption);
                
                em.persist(newRedemption);
                em.flush();
                
                return newRedemption;
            } 
            catch (CustomerNotFoundException | DealNotFoundException ex) 
            {
                throw new CreateNewRedemptionException(ex.getMessage());
            } 
        }
        else 
        {
            throw new CreateNewRedemptionException("Redemption info missing");
        }
    }
    
    @Override
    public List<Redemption> retrieveAllRedemptions()
    {
        Query query = em.createQuery("SELECT r FROM Redemption r ORDER BY r.redemptionId ASC");
        List<Redemption> redemptions = query.getResultList();

        for (Redemption redemption : redemptions) {
            redemption.getCustomer();
            redemption.getDeal();
        }

        return redemptions;
    
    }
    
    @Override
    public Redemption retrieveRedemptionByRedemptionId(Long redemptionId) throws RedemptionNotFoundException {
        Redemption redemption = em.find(Redemption.class, redemptionId);

        if (redemption != null) {
            redemption.getCustomer();
            redemption.getDeal();
            
            return redemption;
        } else {
            throw new RedemptionNotFoundException("Redemption Id " + redemptionId + " does not exist!");
        }
    }
    
    @Override
    public List<Redemption> retrieveRedemptionByDealId(Long dealId) {
        Query query = em.createQuery("SELECT r FROM Redemption r WHERE r.deal.dealId = :inDealId");
        query.setParameter("inDealId", dealId);

        List<Redemption> redemptions = query.getResultList();

        for (Redemption redemption : redemptions) {
            redemption.getCustomer();
            redemption.getDeal();
        }

        return redemptions;
    }
    
    @Override
    public List<Redemption> retrieveRedemptionByCustomerId(Long customerId) {
        Query query = em.createQuery("SELECT r FROM Redemption r WHERE r.customer.id = :inCustomerId");
        query.setParameter("inCustomerId", customerId);

        List<Redemption> redemptions = query.getResultList();

        for (Redemption redemption : redemptions) {
            redemption.getCustomer();
            redemption.getDeal();
        }

        return redemptions;
    }
    
    @Override
    public Integer countTheNumberOfRedemptionByDealId(Long dealId)
    {
        Query query = em.createQuery("SELECT COUNT(r) FROM Redemption r WHERE r.deal.dealId =:inDealId and r.redeemed = true");
        query.setParameter("inDealId", dealId);
        
        Long numberOfRedemptions = (Long)query.getSingleResult();
        Integer numberOfRed = numberOfRedemptions.intValue();
        
        return numberOfRed;
    }
    
    @Override
    public Redemption updateRedemption(Redemption newR) throws RedemptionNotFoundException
    {
        try
        {
            Redemption r = retrieveRedemptionByRedemptionId(newR.getRedemptionId());
            r.setCustomer(newR.getCustomer());
            r.setDeal(newR.getDeal());
            r.setRedeemed(newR.getRedeemed());
            return r;
        }
        catch (RedemptionNotFoundException ex) {
            throw new RedemptionNotFoundException(ex.getMessage());
        }
    }
    
    @Override
    public List<Redemption> retrieveRedemptionsByCustIdandBizId(Long custId, Long bizId)
    {
        List<Redemption> validRedemptions = new ArrayList<>();
        List<Redemption> redemptions = retrieveRedemptionByCustomerId(custId);
        for (Redemption r : redemptions)
        {
            if (r.getDeal().getBusiness().getId() == bizId && !r.getRedeemed())
            {
                validRedemptions.add(r);
            }
        }
        return validRedemptions;
    }
}
