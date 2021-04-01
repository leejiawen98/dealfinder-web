/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Business;
import entity.Customer;
import entity.User;
import java.util.List;
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
import util.exception.BankAccountNotFoundException;
import util.exception.BusinessNotFoundException;
import util.exception.BusinessNotVerifiedException;
import util.exception.BusinessUsernameExistException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateBusinessException;
import util.security.CryptographicHelper;

/**
 *
 * @author yeerouhew
 */
@Stateless
public class BusinessSessionBean implements BusinessSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBean;

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public BusinessSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createBusiness(Business business) throws BusinessUsernameExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Business>> constraintViolations = validator.validate(business);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(business);
                em.flush();

                return business.getId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new BusinessUsernameExistException("Business username " + business.getName() + " exist");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<Business> getAllBusinesses() {
        Query query = em.createQuery("SELECT b FROM Business b");

        return query.getResultList();
    }

    @Override
    public Business getBusinessByBusinessId(Long businessId) throws BusinessNotFoundException {
        Business business = em.find(Business.class, businessId);

        if (business != null) {
            business.getSaleTransactions().size();
            business.getDeals().size();
            business.getBankAccount();
            return business;
        } else {
            throw new BusinessNotFoundException("Business Id " + business.getId() + " does not exist");
        }
    }

    @Override
    public Business getBusinessByUsername(String username) throws BusinessNotFoundException {
        Query query = em.createQuery("SELECT b FROM Business b WHERE b.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            Business business = (Business) query.getSingleResult();
            business.getSaleTransactions().size();
            business.getDeals().size();
            business.getBankAccount();
            return business;

        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BusinessNotFoundException("Business username " + username + " does not exist!");
        }
    }

    @Override
    public Business businessLogin(String username, String password) throws InvalidLoginCredentialException, BusinessNotVerifiedException {
        try {
            Business business = getBusinessByUsername(username);
            
            if (!business.getVerified())
            {
                throw new BusinessNotVerifiedException("Business Account not verified");
            }
            else
            {
                String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + business.getSalt()));

                if (business.getPassword().equals(passwordHash)) {

                    return business;
                } else {
                    throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
                }
            }
        } catch (BusinessNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    @Override
    public void updateBusiness(Business business) throws BusinessNotFoundException, UpdateBusinessException, InputDataValidationException {
        if (business != null && business.getId() != null) {

            Set<ConstraintViolation<Business>> constraintViolations = validator.validate(business);

            if (constraintViolations.isEmpty()) {
                Business businessToUpdate = getBusinessByBusinessId(business.getId());

                if (businessToUpdate.getUsername().equals(business.getUsername())) {
                    businessToUpdate.setAddress(business.getAddress());
                    businessToUpdate.setEmail(business.getEmail());
                    businessToUpdate.setMobileNum(business.getMobileNum());
                    businessToUpdate.setName(business.getName());
                    businessToUpdate.setVerified(business.getVerified());
                } else {
                    throw new UpdateBusinessException("Username of businesss record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations)); 
            }
        } else {
            throw new BusinessNotFoundException("Business ID not provided for business to be updated");
        }
    }

    @Override
    public void deleteBusiness(Long businessId) throws BusinessNotFoundException {
        Business businessToRemove = getBusinessByBusinessId(businessId);
        
        if(businessToRemove.getSaleTransactions().isEmpty()){
            if (businessToRemove.getBankAccount() == null)
            {
                em.remove(businessToRemove);
            }
            else
            {
                try
                {
                    bankAccountSessionBean.deleteBankAccount(businessToRemove.getBankAccount().getAccId());
                    em.remove(businessToRemove);
                }
                catch (BankAccountNotFoundException ex)
                {
                    em.remove(businessToRemove);
                }
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Business>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    
    
    @Override
    public List<Business> getAllNonVerifiedBusinesses() {
        Query query = em.createQuery("SELECT b FROM Business b WHERE b.verified = FALSE");

        return query.getResultList();
    }
}
