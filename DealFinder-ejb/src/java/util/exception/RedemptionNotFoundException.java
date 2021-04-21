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
public class RedemptionNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>RedemptionNotFoundException</code>
     * without detail message.
     */
    public RedemptionNotFoundException() {
    }

    /**
     * Constructs an instance of <code>RedemptionNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RedemptionNotFoundException(String msg) {
        super(msg);
    }
}
