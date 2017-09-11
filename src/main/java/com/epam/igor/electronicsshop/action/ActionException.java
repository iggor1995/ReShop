package com.epam.igor.electronicsshop.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by User on 03.08.2017.
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
