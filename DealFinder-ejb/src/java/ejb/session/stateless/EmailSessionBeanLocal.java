/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Business;
import javax.ejb.Local;
import util.exception.EmailException;

/**
 *
 * @author yeerouhew
 */
@Local
public interface EmailSessionBeanLocal {

    public Boolean emailBusinessVerification(Business business, String emailBody) throws EmailException;
    
}
