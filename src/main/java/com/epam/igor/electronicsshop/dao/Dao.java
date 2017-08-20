package com.epam.igor.electronicsshop.dao;

import com.mysql.jdbc.Connection;

/**
 * Created by User on 01.08.2017.
 */
public abstract class Dao {
    private static final boolean ACTIVE = true;
    private static final boolean INACTIVE = false;
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
