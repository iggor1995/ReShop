package com.epam.igor.electronicsshop.pool;

public class ConnectionPoolException extends Exception {

    public ConnectionPoolException(Exception exception, String message ){
        super(message, exception);
    }
    public ConnectionPoolException(String message){
        super(message);
    }
}
