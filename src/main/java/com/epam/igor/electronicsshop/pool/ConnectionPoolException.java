package com.epam.igor.electronicsshop.pool;

import com.epam.igor.electronicsshop.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by User on 01.08.2017.
 */
public class ConnectionPoolException extends RuntimeException {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceException.class);

    public ConnectionPoolException(Exception exeption, String message ){
        super(message, exeption);
        LOG.error("Connection pool exception has been caught", message);
    }
    public ConnectionPoolException(String message){
        super(message);
        LOG.error(message);
    }
}
