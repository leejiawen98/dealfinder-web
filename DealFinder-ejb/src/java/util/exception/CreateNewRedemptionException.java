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
public class CreateNewRedemptionException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewRedemptionException</code>
     * without detail message.
     */
    public CreateNewRedemptionException() {
    }

    /**
     * Constructs an instance of <code>CreateNewRedemptionException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewRedemptionException(String msg) {
        super(msg);
    }
}
