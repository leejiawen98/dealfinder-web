/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Business;
import java.util.List;
import javax.ejb.Local;
import util.exception.BusinessNotFoundException;
import util.exception.BusinessUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateBusinessException;

/**
 *
 * @author yeerouhew
 */
@Local
public interface BusinessSessionBeanLocal {

    public Long createBusiness(Business business) throws BusinessUsernameExistException, UnknownPersistenceException, InputDataValidationException;

    public List<Business> getAllBusinesses();

    public Business getBusinessByBusinessId(Long businessId) throws BusinessNotFoundException;

    public Business businessLogin(String username, String password) throws InvalidLoginCredentialException;

    public Business getBusinessByUsername(String username) throws BusinessNotFoundException;

    public void updateBusiness(Business business) throws BusinessNotFoundException, UpdateBusinessException, InputDataValidationException;

    public void deleteBusiness(Long businessId) throws BusinessNotFoundException;
    
}
