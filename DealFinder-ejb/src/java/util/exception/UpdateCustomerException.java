/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author yeerouhew
 */
public class UpdateCustomerException extends Exception {

    /**
     * Creates a new instance of <code>UpdateCustomerException</code> without
     * detail message.
     */
    public UpdateCustomerException() {
    }

    /**
     * Constructs an instance of <code>UpdateCustomerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateCustomerException(String msg) {
        super(msg);
    }
}
