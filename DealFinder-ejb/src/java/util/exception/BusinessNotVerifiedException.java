/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author leejiawen98
 */
public class BusinessNotVerifiedException extends Exception {

    /**
     * Creates a new instance of <code>BusinessNotVerified</code> without detail
     * message.
     */
    public BusinessNotVerifiedException() {
    }

    /**
     * Constructs an instance of <code>BusinessNotVerified</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BusinessNotVerifiedException(String msg) {
        super(msg);
    }
}
