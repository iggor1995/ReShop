package com.epam.igor.electronicsshop.pool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.mysql.fabric.jdbc.FabricMySQLDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class creates connection pool with 10 connections
 *
 * @author Igor Lapin
 */
public class ConnectionPool {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionPool.class);
    private static final String FREE_CONNECTIONS = "Free connections: ";
    private static final String USED_CONNECTIONS = " Used connections: ";
    private static final String CANNOT_REPLACE_CONNECTION = "Cannot replace connection";
    private static final String CANNOT_CREATE_DRIVER = "Cannot create driver";
    private static final String CREATING_AND_REGISTERING_NEW_DRIVER = "Creating and registering new driver";
    private static final String PROPERTY_HAVE_NOT_ANY_PARAMETERS = "Property have not any parameters";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String DRIVER = "driver";
    private static final String CONNECTIONS = "connections";
    private static final String SET_INFORMATION_ABOUT_DB_TO_INSTANCE = "Set information about DB to instance";
    private static final String CANNOT_LOAD_PROPERTIES = "Cannot load properties";
    private static final String LOAD_PROPERTY_FILE_WITH_INFORMATION_ABOUT_DB = "Load property file with information about DB";
    private static final String DATABASE_DATABASE_PROPERTIES = "database/database.properties";
    private static final String CANNOT_CREATE_CONNECTION_POOL_INSTANCE = "Cannot create connection pool instance";
    private static final String CANNOT_RELEASE_CONNECTION = "cannot release connection";
    private static final String COULD_NOT_CLOSE_CONNECTION = "Could not close connection";
    private static final String COULDN_T_GET_CONNECTION_OR_ADD_TO_CONNECTIONS_LIST = "Couldn't get connection or add to connections list";
    private static final String COULDN_T_CREATE_DRIVER = "Couldn't create driver";

    private String url;
    private String username;
    private String password;
    private String driver;
    private int connectionsLimit;
    private BlockingQueue<Connection> freeConnections = null;
    private BlockingQueue<Connection> usedConnections = null;

    public ConnectionPool() {
        try {
            loadDBProperties();
            registerDriver();
            initializePool();
        } catch (ConnectionPoolException e) {
            LOG.error(CANNOT_CREATE_CONNECTION_POOL_INSTANCE, e);
        }
    }

    private void loadDBProperties() throws ConnectionPoolException {
        Properties properties = new Properties();
        try {
            properties.load(ConnectionPool.class.getClassLoader().getResourceAsStream(DATABASE_DATABASE_PROPERTIES));
            LOG.info(LOAD_PROPERTY_FILE_WITH_INFORMATION_ABOUT_DB);
        } catch (IOException e) {
            LOG.info(CANNOT_LOAD_PROPERTIES, e);
            throw new ConnectionPoolException(e, CANNOT_LOAD_PROPERTIES);
        }
        if (!properties.isEmpty()) {
            LOG.info(SET_INFORMATION_ABOUT_DB_TO_INSTANCE);
            setUrl(properties.getProperty("url"));
            setUsername(properties.getProperty(USERNAME));
            setPassword(properties.getProperty(PASSWORD));
            setDriver(properties.getProperty(DRIVER));
            setConnectionsLimit(Integer.parseInt(properties.getProperty(CONNECTIONS)));
        } else {
            LOG.error(PROPERTY_HAVE_NOT_ANY_PARAMETERS);
        }
    }

    private void registerDriver() throws ConnectionPoolException {
        try {
            LOG.info(CREATING_AND_REGISTERING_NEW_DRIVER);
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            LOG.info(COULDN_T_CREATE_DRIVER, e);
            throw new ConnectionPoolException(e, CANNOT_CREATE_DRIVER);
        }
    }

    private void initializePool() throws ConnectionPoolException {
        if (freeConnections == null) {
            freeConnections = new ArrayBlockingQueue<>(connectionsLimit);
        }
        if (usedConnections == null) {
            usedConnections = new ArrayBlockingQueue<>(connectionsLimit);
        }
        try {
            Class.forName(driver);
            for (int i = 0; i < connectionsLimit; i++) {
                Connection connection = DriverManager.getConnection(url, username, password);
                freeConnections.put(connection);
            }
        } catch (InterruptedException | ClassNotFoundException | SQLException e) {
            LOG.info(COULDN_T_GET_CONNECTION_OR_ADD_TO_CONNECTIONS_LIST, e);
            throw new ConnectionPoolException(e, COULDN_T_GET_CONNECTION_OR_ADD_TO_CONNECTIONS_LIST);
        }
    }

    public synchronized Connection getConnection() throws ConnectionPoolException {
        Connection currentConnection;
        LOG.info(FREE_CONNECTIONS + freeConnections.size() + USED_CONNECTIONS + usedConnections.size());
        try {
            currentConnection = freeConnections.take();
            usedConnections.put(currentConnection);
        } catch (InterruptedException e) {
            LOG.info(CANNOT_REPLACE_CONNECTION, e);
            throw new ConnectionPoolException(e, CANNOT_REPLACE_CONNECTION);
        }
        LOG.info(FREE_CONNECTIONS + freeConnections.size() + USED_CONNECTIONS + usedConnections.size());
        return currentConnection;
    }

    public synchronized void closeConnection(Connection connection) throws ConnectionPoolException {
        try {
            usedConnections.remove(connection);
            freeConnections.put(connection);
        } catch (InterruptedException e) {
            LOG.info(CANNOT_RELEASE_CONNECTION, e);
            throw new ConnectionPoolException(e, CANNOT_RELEASE_CONNECTION);
        }
    }

    private void closeAllConnectionsInQueue(BlockingQueue<Connection> connections) throws ConnectionPoolException {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.info(COULD_NOT_CLOSE_CONNECTION, e);
                throw new ConnectionPoolException(e, COULD_NOT_CLOSE_CONNECTION);
            }
        }
    }

    public static synchronized ConnectionPool getInstance() {
        return InstanceHolder.instance;
    }

    public static class InstanceHolder {
        static ConnectionPool instance;

        public static void setInstance(ConnectionPool connectionPool) {
            instance = connectionPool;
        }

    }

    public void close() throws ConnectionPoolException {
        closeAllConnectionsInQueue(freeConnections);
        closeAllConnectionsInQueue(usedConnections);

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    private void setDriver(String driver) {
        this.driver = driver;
    }

    public int getConnectionsLimit() {
        return connectionsLimit;
    }

    private void setConnectionsLimit(int connectionsLimit) {
        this.connectionsLimit = connectionsLimit;
    }

    public BlockingQueue<Connection> getFreeConnections() {
        return freeConnections;
    }

    public void setFreeConnections(BlockingQueue<Connection> freeConnections) {
        this.freeConnections = freeConnections;
    }

    public BlockingQueue<Connection> getUsedConnections() {
        return usedConnections;
    }

    public void setUsedConnections(BlockingQueue<Connection> usedConnections) {
        this.usedConnections = usedConnections;
    }


}
