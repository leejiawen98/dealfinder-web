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
public class EmailException extends Exception {

    /**
     * Creates a new instance of <code>EmailException</code> without detail
     * message.
     */
    public EmailException() {
    }

    /**
     * Constructs an instance of <code>EmailException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public EmailException(String msg) {
        super(msg);
    }
}
