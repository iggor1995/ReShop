package com.epam.igor.electronicsshop.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by User on 01.08.2017.
 */
public class DaoException extends Exception {
    private static final Logger LOG = LoggerFactory.getLogger(DaoException.class);

    public DaoException(String message) {
        super(message);
        LOG.error("Dao exception has been caught", message);
    }

    public DaoException(String message, Exception e) {
        super(message, e);
        LOG.error(message ,e);
    }
}