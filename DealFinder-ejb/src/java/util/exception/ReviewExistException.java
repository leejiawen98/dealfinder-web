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
public class ReviewExistException extends Exception {

    /**
     * Creates a new instance of <code>ReviewExistException</code> without
     * detail message.
     */
    public ReviewExistException() {
    }

    /**
     * Constructs an instance of <code>ReviewExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ReviewExistException(String msg) {
        super(msg);
    }
}
