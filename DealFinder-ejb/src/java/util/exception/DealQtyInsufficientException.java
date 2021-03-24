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
public class DealQtyInsufficientException extends Exception {

    /**
     * Creates a new instance of <code>DealQtyInsufficientException</code>
     * without detail message.
     */
    public DealQtyInsufficientException() {
    }

    /**
     * Constructs an instance of <code>DealQtyInsufficientException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DealQtyInsufficientException(String msg) {
        super(msg);
    }
}
