package com.epam.igor.electronicsshop.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by User on 03.08.2017.
 */
public class ActionException extends Exception {
    private static final Logger LOG = LoggerFactory.getLogger(ActionException.class);

    public ActionException(String message) {
        super(message);
        LOG.error("Action exception has been caught", message);
    }

    public ActionException(String message, Exception e) {
        super(message, e);
        LOG.error(message ,e);
    }
}
