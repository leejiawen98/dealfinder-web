/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BankAccount;
import javax.ejb.Local;
import util.exception.BankAccountNotFoundException;
import util.exception.CreateNewBankAccountException;
import util.exception.InputDataValidationException;

/**
 *
 * @author Aaron Tan
 */
@Local
public interface BankAccountSessionBeanLocal {
    public BankAccount createNewBankAccountEntityForBusiness(BankAccount newBankAccount, Long businessId) throws InputDataValidationException, CreateNewBankAccountException;
    
    public BankAccount retrieveBankAccountByBankAccountId(Long bankAccountId) throws BankAccountNotFoundException;
    
    public BankAccount retrieveBankAccountByBusinessId(Long businessId) throws BankAccountNotFoundException;
    
    public void deleteBankAccount(Long bankAccountId) throws BankAccountNotFoundException;
        
}
