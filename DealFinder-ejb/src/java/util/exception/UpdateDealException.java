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
public class UpdateDealException extends Exception {

    /**
     * Creates a new instance of <code>UpdateDealException</code> without detail
     * message.
     */
    public UpdateDealException() {
    }

    /**
     * Constructs an instance of <code>UpdateDealException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateDealException(String msg) {
        super(msg);
    }
}
