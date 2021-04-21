/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Aaron Tan
 */
public class UpdateCreditCardException extends Exception {

    /**
     * Creates a new instance of <code>UpdateCreditCardException</code> without
     * detail message.
     */
    public UpdateCreditCardException() {
    }

    /**
     * Constructs an instance of <code>UpdateCreditCardException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateCreditCardException(String msg) {
        super(msg);
    }
}
