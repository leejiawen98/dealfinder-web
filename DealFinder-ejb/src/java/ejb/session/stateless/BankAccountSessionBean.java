/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BankAccount;
import entity.Business;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.BankAccountNotFoundException;
import util.exception.BusinessNotFoundException;
import util.exception.CreateNewBankAccountException;
import util.exception.InputDataValidationException;

/**
 *
 * @author Aaron Tan
 */
@Stateless
public class BankAccountSessionBean implements BankAccountSessionBeanLocal {

    @EJB(name = "BusinessSessionBeanLocal")
    private BusinessSessionBeanLocal businessSessionBeanLocal;

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public BankAccountSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public BankAccount createNewBankAccountEntityForBusiness(BankAccount newBankAccount, Long businessId) throws InputDataValidationException, CreateNewBankAccountException {
        Set<ConstraintViolation<BankAccount>> constraintViolations = validator.validate(newBankAccount);

        if (constraintViolations.isEmpty()) {
            try {
                if (businessId == null) {
                    throw new CreateNewBankAccountException("A new bank account must be associated with a business");
                }

                Business business = businessSessionBeanLocal.getBusinessByBusinessId(businessId);
                
                newBankAccount.setBusiness(business);
                business.setBankAccount(newBankAccount);              

                em.persist(newBankAccount);
                em.flush();

                return newBankAccount;
            } catch (BusinessNotFoundException ex) {
                throw new CreateNewBankAccountException("An error has occured while creating the new bank account: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public BankAccount retrieveBankAccountByBankAccountId(Long bankAccountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = em.find(BankAccount.class, bankAccountId);

        if (bankAccount != null) {
            return bankAccount;
        } else {
            throw new BankAccountNotFoundException("Bank Account ID " + bankAccountId + " does not exist!");
        }
    }
    
    @Override
    public BankAccount retrieveBankAccountByBusinessId(Long businessId) throws BankAccountNotFoundException {
        Query query = em.createQuery("SELECT ba FROM BankAccount ba WHERE ba.business.id = :inBusinessId");
        query.setParameter("inBusinessId", businessId);
        
        try {
            return (BankAccount) query.getSingleResult();

        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BankAccountNotFoundException("Bank Account for Business Id " + businessId + " does not exist!");
        }
    }
    
    @Override
    public void deleteBankAccount(Long bankAccountId) throws BankAccountNotFoundException {
        try
        {
            BankAccount bankAccountToRemove = retrieveBankAccountByBankAccountId(bankAccountId);

            if (bankAccountToRemove.getBusiness() != null) {
                Business business = bankAccountToRemove.getBusiness();

                business.setBankAccount(null);
                bankAccountToRemove.setBank(null);
            }

            em.remove(bankAccountToRemove);
        }
        catch (BankAccountNotFoundException ex)
        {
            throw new BankAccountNotFoundException(ex.getMessage());
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<BankAccount>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
