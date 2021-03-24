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
public class DealNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>DealNotFoundException</code> without
     * detail message.
     */
    public DealNotFoundException() {
    }

    /**
     * Constructs an instance of <code>DealNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DealNotFoundException(String msg) {
        super(msg);
    }
}
