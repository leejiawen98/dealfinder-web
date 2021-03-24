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
public class BusinessUsernameExistException extends Exception {

    /**
     * Creates a new instance of <code>BusinessUsernameExistException</code>
     * without detail message.
     */
    public BusinessUsernameExistException() {
    }

    /**
     * Constructs an instance of <code>BusinessUsernameExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public BusinessUsernameExistException(String msg) {
        super(msg);
    }
}
