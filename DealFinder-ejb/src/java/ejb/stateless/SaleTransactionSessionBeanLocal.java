/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Deal;
import entity.SaleTransaction;
import java.util.List;
import javax.ejb.Local;
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
    public SaleTransaction createNewSaleTransaction(Long customerId, SaleTransaction newSaleTransactionEntity) throws CustomerNotFoundException, CreateNewSaleTransactionException; 
    
    public List<SaleTransaction> retrieveAllSaleTransactions();
    
    public SaleTransaction retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException;
    
    public void updateSaleTransaction(SaleTransaction saleTransactionEntity);
    
    public void deleteSaleTransaction(SaleTransaction saleTransactionEntity) throws DeleteSaleTransactionException;
    
    public List<SaleTransaction> retrieveSaleTransactionDealByDealId(Long dealId) throws DealNotFoundException;
}
