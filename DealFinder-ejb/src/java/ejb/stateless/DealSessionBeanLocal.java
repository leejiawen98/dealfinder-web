/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Deal;
import java.util.List;
import javax.ejb.Local;
import util.exception.DealNotFoundException;
import util.exception.DealQtyInsufficientException;

/**
 *
 * @author yeerouhew
 */
@Local
public interface DealSessionBeanLocal {

    public List<Deal> retrieveAllDeals();

    public List<Deal> searchDealByName(String searchString);

    public Deal getDealByDealId(Long dealId) throws DealNotFoundException;

    public Deal getDealByDealSerialNum(String serialNum) throws DealNotFoundException;

    public void debitQtyOnHand(Long dealId, Integer qtyToDebit) throws DealQtyInsufficientException, DealNotFoundException;

    public void creditQtyOnHand(Long dealId, Integer qtyToCredit) throws DealNotFoundException;
    
}
