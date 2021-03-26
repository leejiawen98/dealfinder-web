/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Customer;
import entity.Deal;
import entity.SaleTransaction;
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
    public SaleTransaction createNewSaleTransaction(Long customerId, SaleTransaction newSaleTransactionEntity) throws CustomerNotFoundException, CreateNewSaleTransactionException
    {
        if(newSaleTransactionEntity != null)
        {
            try
            {
                Customer customerEntity = customerSessionBeanLocal.getCustomerByCustomerId(customerId);
                newSaleTransactionEntity.setUser(customerEntity);
                customerEntity.getSaleTransactions().add(newSaleTransactionEntity);
                
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
    
//    @Override
//    public void voidRefundSaleTransaction(Long saleTransactionId) throws SaleTransactionNotFoundException, SaleTransactionAlreadyVoidedRefundedException
//    {
//        SaleTransaction saleTransactionEntity = retrieveSaleTransactionBySaleTransactionId(saleTransactionId);
//        
//        if(!saleTransactionEntity.getVoidRefund())
//        {
//            for(SaleTransactionLineItemEntity saleTransactionLineItemEntity:saleTransactionEntity.getSaleTransactionLineItemEntities())
//            {
//                try
//                {
//                    productEntitySessionBeanLocal.creditQuantityOnHand(saleTransactionLineItemEntity.getProductEntity().getProductId(), saleTransactionLineItemEntity.getQuantity());
//                }
//                catch(ProductNotFoundException ex)
//                {
//                    ex.printStackTrace(); // Ignore exception since this should not happen
//                }                
//            }
//            
//            saleTransactionEntity.setVoidRefund(true);
//        }
//        else
//        {
//            throw new SaleTransactionAlreadyVoidedRefundedException("The sale transaction has aready been voided/refunded");
//        }
//    }
}
