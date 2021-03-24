/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Business;
import entity.Customer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.BusinessNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateBusinessException;
import util.exception.UpdateCustomerException;
import util.security.CryptographicHelper;

/**
 *
 * @author yeerouhew
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    @Override
    public Long createCustomer(Customer customer) throws CustomerUsernameExistException, UnknownPersistenceException {
        try {
            em.persist(customer);
            em.flush();

            return customer.getId();

        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CustomerUsernameExistException("Customer username " + customer.getId() + " exist");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        Query query = em.createQuery("SELECT c FROM Customer c");

        return query.getResultList();
    }

    @Override
    public Customer getCustomerByCustomerId(Long customerId) throws CustomerNotFoundException {
        Customer customer = em.find(Customer.class, customerId);

        if (customer != null) {
            return customer;
        } else {
            throw new CustomerNotFoundException("Customer Id " + customer.getId() + " does not exist");
        }
    }

    @Override
    public Customer getCustomerByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Customer) query.getSingleResult();

        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer username " + username + " does not exist!");
        }
    }

    @Override
    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Customer customer = getCustomerByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + customer.getSalt()));
            
            if (customer.getPassword().equals(passwordHash)) {
                return customer;
                
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public void updateCustomer(Customer customer) throws UpdateCustomerException, CustomerNotFoundException {
        if(customer != null && customer.getId() != null){
            Customer customerToUpdate = getCustomerByCustomerId(customer.getId());
            
            if(customerToUpdate.getUsername().equals(customerToUpdate.getUsername())){
                customerToUpdate.setEmail(customer.getEmail());
                customerToUpdate.setFirstName(customer.getFirstName());
                customerToUpdate.setLastName(customer.getLastName());
                customerToUpdate.setMobileNum(customer.getMobileNum());

            } else {
                throw new UpdateCustomerException("Username of customer record to be updated does not match the existing record");
            }
        } else {
            throw new CustomerNotFoundException("Customer ID not provided for customer to be updated");
        }
    }
    
    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException{
        Customer customerToRemove = getCustomerByCustomerId(customerId);
        
        em.remove(customerToRemove);
    }
    
}
