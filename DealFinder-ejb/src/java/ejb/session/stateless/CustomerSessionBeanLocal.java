/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.DeleteCustomerException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author yeerouhew
 */
@Local
public interface CustomerSessionBeanLocal {

    public Long createCustomer(Customer customer) throws CustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException;

    public List<Customer> getAllCustomers();

    public Customer getCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException;

    public void updateCustomer(Customer customer) throws UpdateCustomerException, CustomerNotFoundException, InputDataValidationException;

    public void deleteCustomer(Long customerId) throws CustomerNotFoundException, DeleteCustomerException;

    public Customer getCustomerByUsername(String username) throws CustomerNotFoundException;
    
}
