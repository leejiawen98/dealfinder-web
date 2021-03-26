/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import javax.ejb.Local;
import util.exception.CreateNewCreditCardException;
import util.exception.CreditCardNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author Aaron Tan
 */
@Local
public interface CreditCardSessionBeanLocal {
    public CreditCard createNewCreditCardEntityForCustomer(CreditCard newCreditCard, Long customerId) throws InputDataValidationException, CreateNewCreditCardException;
    
    public CreditCard retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException;
    
    public CreditCard retrieveCreditCardByCustomerId(Long customerId) throws CreditCardNotFoundException;
    
    public void deleteCreditCard(Long creditCardId) throws CreditCardNotFoundException;
    
    
}
