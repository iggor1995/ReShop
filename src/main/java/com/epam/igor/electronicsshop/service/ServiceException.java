package com.epam.igor.electronicsshop.service;

public class ServiceException extends Exception {

    public ServiceException(Exception exception, String message ){
        super(message, exception);
    }

    public ServiceException(String message){
        super(message);
    }
}
