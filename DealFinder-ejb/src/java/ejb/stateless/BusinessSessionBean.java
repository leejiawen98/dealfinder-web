/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Business;
import entity.Customer;
import entity.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.BusinessNotFoundException;
import util.exception.BusinessUsernameExistException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
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

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    @Override
    public Long createBusiness(Business business) throws BusinessUsernameExistException, UnknownPersistenceException{
        try{
            em.persist(business);
            em.flush();
            
            return business.getId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new BusinessUsernameExistException("Business username " + business.getId() + " exist");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
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
            return (Business) query.getSingleResult();

        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BusinessNotFoundException("Business username " + username + " does not exist!");
        }
    }

    
    @Override
    public Business businessLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Business business = getBusinessByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + business.getSalt()));

            if (business.getPassword().equals(passwordHash)) {

                return business;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (BusinessNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public void updateBusiness(Business business) throws BusinessNotFoundException, UpdateBusinessException{
        if(business != null && business.getId() != null){
            Business businessToUpdate = getBusinessByBusinessId(business.getId());
            
            if(businessToUpdate.getUsername().equals(business.getUsername())){
                businessToUpdate.setAddress(business.getAddress());
                businessToUpdate.setEmail(business.getEmail());
                businessToUpdate.setMobileNum(business.getMobileNum());
                businessToUpdate.setName(business.getName());
            } else {
                throw new UpdateBusinessException("Username of businesss record to be updated does not match the existing record");
            }
        } else {
            throw new BusinessNotFoundException("Business ID not provided for business to be updated");
        }
    }
    
    @Override
    public void deleteBusiness(Long businessId) throws BusinessNotFoundException{
        Business businessToRemove = getBusinessByBusinessId(businessId);
        
        em.remove(businessToRemove);
    }
    
    
}
