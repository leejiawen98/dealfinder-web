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
public class DeleteCustomerException extends Exception {

    /**
     * Creates a new instance of <code>DeleteCustomerException</code> without
     * detail message.
     */
    public DeleteCustomerException() {
    }

    /**
     * Constructs an instance of <code>DeleteCustomerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteCustomerException(String msg) {
        super(msg);
    }
}
