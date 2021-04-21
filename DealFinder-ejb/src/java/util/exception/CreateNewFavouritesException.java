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
public class CreateNewFavouritesException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewFavouritesException</code>
     * without detail message.
     */
    public CreateNewFavouritesException() {
    }

    /**
     * Constructs an instance of <code>CreateNewFavouritesException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewFavouritesException(String msg) {
        super(msg);
    }
}
