/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Deal;
import java.util.List;
import javax.ejb.Local;
import util.exception.BusinessNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewDealException;
import util.exception.DealNotFoundException;
import util.exception.DealQtyInsufficientException;
import util.exception.DealSerialNumberExistException;
import util.exception.DeleteDealException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDealException;

/**
 *
 * @author yeerouhew
 */
@Local
public interface DealSessionBeanLocal {

    public Deal createNewDeal(Deal newDeal, Long categoryId, List<Long> tagIds, Long businessId) throws CreateNewDealException, CategoryNotFoundException, DealSerialNumberExistException, UnknownPersistenceException, InputDataValidationException, BusinessNotFoundException;
    
    public List<Deal> retrieveAllDeals();

    public List<Deal> searchDealByName(String searchString);

    public void debitQtyOnHand(Long dealId, Integer qtyToDebit) throws DealQtyInsufficientException, DealNotFoundException;

    public void creditQtyOnHand(Long dealId, Integer qtyToCredit) throws DealNotFoundException;
    
    public List<Deal> filterDealByCategory(Long categoryId) throws CategoryNotFoundException;
    
    public List<Deal> filterDealByTags(List<Long> tagIds, String condition);
    
    public Deal retrieveDealByDealId(Long dealId) throws DealNotFoundException;
    
    public Deal retrieveDealByDealSerialNum(String serialNum) throws DealNotFoundException;
    
    public void updateDeal(Deal deal, Long categoryId, List<Long> tagIds) throws DealNotFoundException, InputDataValidationException, UpdateDealException, TagNotFoundException, CategoryNotFoundException;
   
    public void deleteDeal(Long dealId) throws DeleteDealException, DealNotFoundException;
}
