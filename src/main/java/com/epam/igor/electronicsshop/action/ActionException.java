package com.epam.igor.electronicsshop.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 /**
 * Class sets necessary attributes for displaying edit user page
 * @author Igor Lapin
 */
public class ActionException extends Exception {
    private static final Logger LOG = LoggerFactory.getLogger(ActionException.class);
    private static final String ACTION_ERROR = "Action exception has been caught";

    public ActionException(String message) {
        super(message);
        LOG.error(ACTION_ERROR, message);
    }

    public ActionException(String message, Exception e) {
        super(message, e);
        LOG.error(message ,e);
    }
}
