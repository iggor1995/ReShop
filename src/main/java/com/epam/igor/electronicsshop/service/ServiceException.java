package com.epam.igor.electronicsshop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by User on 03.08.2017.
 */
public class ServiceException extends Exception {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceException.class);
    public static final String SERVICE_EXCEPTION_HAS_BEEN_CAUGHT = "Service exception has been caught";

    public ServiceException(Exception exeption, String message ){
        super(message, exeption);
        LOG.error(SERVICE_EXCEPTION_HAS_BEEN_CAUGHT, message);
    }
    public ServiceException(String message){
        super(message);
        LOG.error(message);
    }
}
