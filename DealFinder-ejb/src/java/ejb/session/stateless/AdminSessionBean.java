/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Admin;
import entity.Customer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UpdateCustomerException;
import util.security.CryptographicHelper;

/**
 *
 * @author yeerouhew
 */
@Stateless
public class AdminSessionBean implements AdminSessionBeanLocal {

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    @Override
    public Long createAdmin(Admin admin) {
        em.persist(admin);
        em.flush();

        return admin.getAdminId();
    }

    @Override
    public List<Admin> getAllAdmins() {
        Query query = em.createQuery("SELECT a FROM Admin A");

        return query.getResultList();
    }

    @Override
    public Admin getAdminByAdminId(Long adminId) {
        Admin admin = em.find(Admin.class, adminId);

        return admin;
    }

    @Override
    public Admin getAdminByUsername(String username) {
        Query query = em.createQuery("SELECT a FROM Admin a WHERE a.username = :inUsername");
        query.setParameter("inUsername", username);

        return (Admin) query.getSingleResult();
    }

    @Override
    public Admin adminLogin(String username, String password) throws InvalidLoginCredentialException {
        Admin admin = getAdminByUsername(username);
        String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + admin.getSalt()));

        if (admin.getPassword().equals(passwordHash)) {
            return admin;
        } else {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
  

}
