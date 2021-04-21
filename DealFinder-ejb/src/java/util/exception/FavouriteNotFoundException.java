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
public class FavouriteNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>FavouriteNotFoundException</code> without
     * detail message.
     */
    public FavouriteNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FavouriteNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public FavouriteNotFoundException(String msg) {
        super(msg);
    }
}
