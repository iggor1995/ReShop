package com.epam.igor.electronicsshop.action;

/**
 * Class sets necessary attributes for displaying edit user page
 *
 * @author Igor Lapin
 */
public class ActionException extends Exception {

    public ActionException(String message) {
        super(message);
    }

    public ActionException(String message, Exception e) {
        super(message, e);
    }
}
