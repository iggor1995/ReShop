package com.epam.igor.electronicsshop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by User on 03.08.2017.
 */
public class ServiceException extends Exception {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceException.class);
    public ServiceException(Exception exeption, String message ){
        super(message, exeption);
        LOG.error("Service exception has been caught", message);
    }
    public ServiceException(String message){
        super(message);
        LOG.error(message);
    }
}
