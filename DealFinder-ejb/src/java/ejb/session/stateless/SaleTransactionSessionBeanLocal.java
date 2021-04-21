/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Deal;
import entity.SaleTransaction;
import java.util.List;
import javax.ejb.Local;
import util.exception.BusinessNotFoundException;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.DealNotFoundException;
import util.exception.DeleteSaleTransactionException;
import util.exception.SaleTransactionNotFoundException;

/**
 *
 * @author Aaron Tan
 */
@Local
public interface SaleTransactionSessionBeanLocal {
    public SaleTransaction createNewSaleTransaction(Long customerId, SaleTransaction newSaleTransactionEntity, Long dealId) throws CustomerNotFoundException, CreateNewSaleTransactionException;
    
    public List<SaleTransaction> retrieveAllSaleTransactions();
    
    public SaleTransaction retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException;
    
    public void updateSaleTransaction(SaleTransaction saleTransactionEntity);
    
    public void deleteSaleTransaction(SaleTransaction saleTransactionEntity) throws DeleteSaleTransactionException;
    
    public List<SaleTransaction> retrieveSaleTransactionDealByDealId(Long dealId) throws DealNotFoundException;
    
    public List<SaleTransaction> retrieveSaleTransactionDealByBusinessId(Long businessId) throws BusinessNotFoundException;
    
    public List<SaleTransaction> retrieveSaleTransactionDealByCustomerId(Long customerId) throws CustomerNotFoundException;
    
    public List<SaleTransaction> retrieveSaleTransactionDealByBusinessAndMonth(Long businessId, String month) throws BusinessNotFoundException;

    public List<SaleTransaction> retrieveSaleTransactionDealByBusinessAndMonthndDeal(Long businessId, String month, Long dealId) throws BusinessNotFoundException;
}
