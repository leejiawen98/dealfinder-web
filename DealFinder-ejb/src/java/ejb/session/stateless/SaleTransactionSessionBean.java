/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Deal;
import entity.SaleTransaction;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.BusinessNotFoundException;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.DealNotFoundException;
import util.exception.DealQtyInsufficientException;
import util.exception.DeleteSaleTransactionException;
import util.exception.SaleTransactionNotFoundException;

/**
 *
 * @author Aaron Tan
 */
@Stateless
public class SaleTransactionSessionBean implements SaleTransactionSessionBeanLocal {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB(name = "DealSessionBeanLocal")
    private DealSessionBeanLocal dealSessionBeanLocal;
       
    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    @Resource
    private EJBContext eJBContext;
    
    public SaleTransactionSessionBean()
    {
    }
    
    @Override
    public SaleTransaction createNewSaleTransaction(Long customerId, SaleTransaction newSaleTransactionEntity, Long dealId) throws CustomerNotFoundException, CreateNewSaleTransactionException
    {
        if(newSaleTransactionEntity != null)
        {
            try
            {
                Customer customerEntity = customerSessionBeanLocal.getCustomerByCustomerId(customerId);
                newSaleTransactionEntity.setUser(customerEntity);
                customerEntity.getSaleTransactions().add(newSaleTransactionEntity);
                
                Deal deal = dealSessionBeanLocal.retrieveDealByDealId(dealId);
                newSaleTransactionEntity.setDeal(deal);
                deal.getSaleTransactions().add(newSaleTransactionEntity);
                
                if (!customerEntity.getDeals().contains(deal))
                {
                    customerEntity.getDeals().add(deal);
                    customerEntity.setDeals(customerEntity.getDeals());
                }

                dealSessionBeanLocal.debitQtyOnHand(newSaleTransactionEntity.getDeal().getDealId(), newSaleTransactionEntity.getQuantity());
                
                em.persist(newSaleTransactionEntity);
                                
                em.flush();

                return newSaleTransactionEntity;
            }
            catch(DealNotFoundException | DealQtyInsufficientException ex)
            {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();
                throw new CreateNewSaleTransactionException(ex.getMessage());
            }
        }
        else
        {
            throw new CreateNewSaleTransactionException("Sale transaction information not provided");
        }
    }
    
    @Override
    public List<SaleTransaction> retrieveAllSaleTransactions()
    {
        Query query = em.createQuery("SELECT st FROM SaleTransaction st");
        
        return query.getResultList();
    }
    
    @Override
    public List<SaleTransaction> retrieveSaleTransactionDealByDealId(Long dealId) throws DealNotFoundException
    {
//        Query query = em.createNamedQuery("selectAllSaleTransactionLineItemsByProductId");
//        query.setParameter("inProductId", productId);
//        
//        return query.getResultList();
        Query query = em.createQuery("SELECT st FROM SaleTransaction st WHERE st.deal.dealId =:inDealId");
        query.setParameter("inDealId", dealId);

        try {
            return query.getResultList();

        } catch (NoResultException | NonUniqueResultException ex) {
            throw new DealNotFoundException("Deal Id " + dealId + " does not exist!");
        }
    }
    
    @Override
    public SaleTransaction retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException
    {
        SaleTransaction saleTransactionEntity = em.find(SaleTransaction.class, saleTransactionId);
        
        if(saleTransactionEntity != null)
        {
//            for(SaleTransactionLineItemEntity saleTransactionLineItemEntity:saleTransactionEntity.getSaleTransactionLineItemEntities())
//            {
//                saleTransactionLineItemEntity.getProductEntity();
//            }
            saleTransactionEntity.getDeal();
            
            
            return saleTransactionEntity;
        }
        else
        {
            throw new SaleTransactionNotFoundException("Sale Transaction ID " + saleTransactionId + " does not exist!");
        }                
    }
    
    @Override
    public void updateSaleTransaction(SaleTransaction saleTransactionEntity)
    {
        em.merge(saleTransactionEntity);
    }
    
    @Override
    public void deleteSaleTransaction(SaleTransaction saleTransactionEntity) throws DeleteSaleTransactionException
    {
        try {
            SaleTransaction saleTransactionToRemove = retrieveSaleTransactionBySaleTransactionId(saleTransactionEntity.getSaleTransactionId());
            saleTransactionToRemove.getDeal().getSaleTransactions().remove(saleTransactionEntity);
            saleTransactionToRemove.setDeal(null);
            
            saleTransactionToRemove.getUser().getSaleTransactions().remove(saleTransactionEntity);
            saleTransactionToRemove.setUser(null);
            
            em.remove(saleTransactionToRemove);
        } catch (SaleTransactionNotFoundException ex) {
            throw new DeleteSaleTransactionException("An error occured while deleting Sale Transaction: " + ex.getMessage());
        }
    }
    
    @Override
    public List<SaleTransaction> retrieveSaleTransactionDealByBusinessId(Long businessId) throws BusinessNotFoundException
    {
        Query query = em.createQuery("SELECT st FROM SaleTransaction st WHERE st.deal.business.id =:inBusinessId");
        query.setParameter("inBusinessId", businessId);

        try {
            return query.getResultList();

        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BusinessNotFoundException("Business Id " + businessId + " does not exist!");
        }
    }
    
    @Override
    public List<SaleTransaction> retrieveSaleTransactionDealByBusinessAndMonth(Long businessId, String month) throws BusinessNotFoundException
    {
        HashMap<String,Integer> hm = new HashMap<String, Integer>();
        hm.put("January", 1);
        hm.put("February", 2);
        hm.put("March", 3);
        hm.put("April", 4);
        hm.put("May", 5);
        hm.put("June", 6);
        hm.put("July", 7);
        hm.put("August", 8);
        hm.put("September", 9);
        hm.put("October", 10);
        hm.put("November", 11);
        hm.put("December", 12);
        
        int m = hm.get(month);
        
        Query query = em.createQuery("SELECT st FROM SaleTransaction st WHERE st.deal.business.id =:inBusinessId and EXTRACT(MONTH FROM st.transactionDateTime) =:inM");
        query.setParameter("inBusinessId", businessId);
        query.setParameter("inM", m);

        try {
            return query.getResultList();

        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BusinessNotFoundException("Business Id " + businessId + " does not exist!");
        }
    }
    
    @Override
    public List<SaleTransaction> retrieveSaleTransactionDealByCustomerId(Long customerId) throws CustomerNotFoundException
    {
        Query query = em.createQuery("SELECT st FROM SaleTransaction st WHERE st.user.id =:inCustomerId");
        query.setParameter("inCustomerId", customerId);

        try {
            return query.getResultList();

        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Id " + customerId + " does not exist!");
        }
    }
    
    @Override
    public List<SaleTransaction> retrieveSaleTransactionDealByBusinessAndMonthndDeal(Long businessId, String month, Long dealId) throws BusinessNotFoundException
    {
        HashMap<String,Integer> hm = new HashMap<String, Integer>();
        hm.put("January", 1);
        hm.put("February", 2);
        hm.put("March", 3);
        hm.put("April", 4);
        hm.put("May", 5);
        hm.put("June", 6);
        hm.put("July", 7);
        hm.put("August", 8);
        hm.put("September", 9);
        hm.put("October", 10);
        hm.put("November", 11);
        hm.put("December", 12);
        
        int m = hm.get(month);
        
        Query query = em.createQuery("SELECT st FROM SaleTransaction st WHERE st.deal.business.id =:inBusinessId and st.deal.dealId =:inDealId and EXTRACT(MONTH FROM st.transactionDateTime) =:inM");
        query.setParameter("inBusinessId", businessId);
        query.setParameter("inDealId", dealId);
        query.setParameter("inM", m);

        try {
            return query.getResultList();

        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BusinessNotFoundException("Business Id " + businessId + " does not exist!");
        }
    }
}
