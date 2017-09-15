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

public class ConnectionPool {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionPool.class);

    private String url;
    private String username;
    private String password;
    private String driver;
    private int connectionsLimit;
    private BlockingQueue<Connection> freeConnections = null;
    private BlockingQueue<Connection> usedConnections = null;

    private ConnectionPool(){
        try {
            loadDBProperties();
            registerDriver();
            initializePool();
        }
        catch (ConnectionPoolException e){
            LOG.error("Cannot create connection pool instance", e);
        }
    }

    private void loadDBProperties() throws ConnectionPoolException{
        Properties properties = new Properties();
        try {
            properties.load(ConnectionPool.class.getClassLoader().getResourceAsStream("database/database.properties"));
            LOG.info("Load property file with information about DB");
        } catch (IOException e) {
            throw new ConnectionPoolException(e, "Cannot load properties");
        }
        if (!properties.isEmpty()) {
            LOG.info("Set information about DB to instance");
            setUrl(properties.getProperty("url"));
            setUsername(properties.getProperty("username"));
            setPassword(properties.getProperty("password"));
            setDriver(properties.getProperty("driver"));
            setConnectionsLimit(Integer.parseInt(properties.getProperty("connections")));
        } else {
            LOG.error("Property have not any parameters");
        }
    }

    private void registerDriver() throws ConnectionPoolException{
        try {
            LOG.info("Creating and registering new driver");
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            throw  new ConnectionPoolException(e, "Cannot create driver");
        }
    }
    private void initializePool() throws ConnectionPoolException{
        if (freeConnections == null) {
            freeConnections = new ArrayBlockingQueue<>(connectionsLimit);
        }
        if(usedConnections == null) {
            usedConnections = new ArrayBlockingQueue<>(connectionsLimit);
        }
        try {
            Class.forName(driver);
        for (int i = 0; i < connectionsLimit; i++) {
                Connection connection = DriverManager.getConnection(url, username, password);
                freeConnections.put(connection);
            }
        } catch (InterruptedException | ClassNotFoundException |SQLException e) {
            e.printStackTrace();
        }
    }
    public synchronized Connection getConnection() throws ConnectionPoolException{
        Connection currentConnection;
        LOG.info("Free connections: " + freeConnections.size() + " Used connections: " + usedConnections.size());
        try {
            currentConnection = freeConnections.take();
            usedConnections.put(currentConnection);
        } catch (InterruptedException e) {
            throw new ConnectionPoolException(e, "Cannot replace connection");
        }
        LOG.info("Free connections: " + freeConnections.size() + " Used connections: " + usedConnections.size());
        return currentConnection;
    }

    public synchronized void closeConnection(Connection connection) throws ConnectionPoolException{
        try {
            usedConnections.remove(connection);
            freeConnections.put(connection);
        } catch (InterruptedException e) {
            throw new ConnectionPoolException(e, "cannot release connection");
        }
    }
    public static synchronized ConnectionPool getInstance(){
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private static ConnectionPool instance = new ConnectionPool();
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

    public void setUsername(String username) {
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

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public int getConnectionsLimit() {
        return connectionsLimit;
    }

    public void setConnectionsLimit(int connectionsLimit) {
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
