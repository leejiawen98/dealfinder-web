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
public class DeleteDealException extends Exception {

    /**
     * Creates a new instance of <code>DeleteDealException</code> without detail
     * message.
     */
    public DeleteDealException() {
    }

    /**
     * Constructs an instance of <code>DeleteDealException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteDealException(String msg) {
        super(msg);
    }
}
