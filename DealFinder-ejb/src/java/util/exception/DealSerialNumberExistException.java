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
public class DealSerialNumberExistException extends Exception {

    /**
     * Creates a new instance of <code>DealSerialNumberExistException</code>
     * without detail message.
     */
    public DealSerialNumberExistException() {
    }

    /**
     * Constructs an instance of <code>DealSerialNumberExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public DealSerialNumberExistException(String msg) {
        super(msg);
    }
}
