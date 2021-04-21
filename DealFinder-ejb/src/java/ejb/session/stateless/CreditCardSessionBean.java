/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import entity.Customer;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewCreditCardException;
import util.exception.CreditCardNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCreditCardException;

/**
 *
 * @author Aaron Tan
 */
@Stateless
public class CreditCardSessionBean implements CreditCardSessionBeanLocal {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CreditCardSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CreditCard createNewCreditCardEntityForCustomer(CreditCard newCreditCard, Long customerId) throws InputDataValidationException, CreateNewCreditCardException {
        Set<ConstraintViolation<CreditCard>> constraintViolations = validator.validate(newCreditCard);

        if (constraintViolations.isEmpty()) {
            try {
                if (customerId == null) {
                    throw new CreateNewCreditCardException("A new credit card must be associated with a customer");
                }

                Customer customer = customerSessionBeanLocal.getCustomerByCustomerId(customerId);

                newCreditCard.setCustomer(customer);
                customer.setCreditCard(newCreditCard);
                //customer.getCreditCards().add(newCreditCard); for many credit cards

                em.persist(newCreditCard);
                em.flush();

                return newCreditCard;
            } catch (CustomerNotFoundException ex) {
                throw new CreateNewCreditCardException("An error has occured while creating the new credit card: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public CreditCard retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException {
        CreditCard creditCard = em.find(CreditCard.class, creditCardId);

        if (creditCard != null) {
            return creditCard;
        } else {
            throw new CreditCardNotFoundException("Credit Card ID " + creditCardId + " does not exist!");
        }
    }

    @Override
    public CreditCard retrieveCreditCardByCustomerId(Long customerId) throws CreditCardNotFoundException {
        Query query = em.createQuery("SELECT c FROM CreditCard c WHERE c.customer.id = :inCustomerId");
        query.setParameter("inCustomerId", customerId);

        try {
            return (CreditCard) query.getSingleResult();

        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CreditCardNotFoundException("Credit Card for Customer Id " + customerId + " does not exist!");
        }
    }

    @Override
    public CreditCard updateCreditCard(CreditCard creditCard) throws CreditCardNotFoundException, InputDataValidationException, UpdateCreditCardException
    {
        Set<ConstraintViolation<CreditCard>> constraintViolations = validator.validate(creditCard);

        if (constraintViolations.isEmpty()) {
            CreditCard creditCardToUpdate = retrieveCreditCardByCreditCardId(creditCard.getCcId());
            if (creditCardToUpdate.getCcNum().equals(creditCard.getCcNum())) {
                creditCardToUpdate.setCcName(creditCard.getCcName());
                creditCardToUpdate.setCcNum(creditCard.getCcNum());
                creditCardToUpdate.setCvv(creditCard.getCvv());
                creditCardToUpdate.setExpDate(creditCard.getExpDate());
                return creditCardToUpdate;
            } else {
                throw new UpdateCreditCardException("Credit Card Number of Credit Card record to be updated does not match the existing record");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public void deleteCreditCard(Long creditCardId) throws CreditCardNotFoundException {
        CreditCard creditCardToRemove = retrieveCreditCardByCreditCardId(creditCardId);

        if (creditCardToRemove.getCustomer() != null) {
            Customer customer = creditCardToRemove.getCustomer();

            customer.setCreditCard(null);
            creditCardToRemove.setCustomer(null);
        }

        em.remove(creditCardToRemove);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CreditCard>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
