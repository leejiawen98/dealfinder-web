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
public class CreateNewDealException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewDealException</code> without
     * detail message.
     */
    public CreateNewDealException() {
    }

    /**
     * Constructs an instance of <code>CreateNewDealException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewDealException(String msg) {
        super(msg);
    }
}
