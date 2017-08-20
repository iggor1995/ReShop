package com.epam.igor.electronicsshop.pool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mysql.fabric.jdbc.FabricMySQLDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by User on 01.08.2017.
 */
public class ConnectionPool {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionPool.class);
    private String url;
    private String username;
    private String password;
    private String driver;
    private int connectionsLimit;
    private int timeout;
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
            LOG.info("Load DB property file");

        } catch (IOException e) {
            throw  new ConnectionPoolException(e, "Cannot load properties");
        }
        if(!properties.isEmpty()){
            LOG.info("Load DB properties to instance");
            setDriver(properties.getProperty("driver"));
            setUrl(properties.getProperty("url"));
            setUsername(properties.getProperty("username"));
            setPassword(properties.getProperty("password"));
            setConnectionsLimit(Integer.parseInt(properties.getProperty("connections.limit")));
            setTimeout(Integer.parseInt(properties.getProperty("connection.time.out")));
        }
        else{
            LOG.error("Properies list is empty");
        }
    }

    private void registerDriver() throws ConnectionPoolException{
        try {
            LOG.info("Creationg and registering new driver");
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            throw  new ConnectionPoolException(e, "Canot create driver");
        }
    }
    private void initializePool() throws ConnectionPoolException{
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new ConnectionPoolException(e, "Cannot find class");
        }
        freeConnections = new ArrayBlockingQueue<Connection>(connectionsLimit);
        usedConnections = new ArrayBlockingQueue<Connection>(connectionsLimit);
        while(freeConnections.size() != connectionsLimit){
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                freeConnections.put(connection);
            } catch (SQLException | InterruptedException e) {
                throw new ConnectionPoolException(e, "Cannot get conection or put into connections");
            }
        }
    }
    public synchronized Connection getConnection() throws ConnectionPoolException{
        Connection currentConnection;
        LOG.info("Free connections" + freeConnections.size() + " Used connections" + usedConnections);
        try {
            currentConnection = freeConnections.poll(timeout, TimeUnit.SECONDS);
            usedConnections.put(currentConnection);
        } catch (InterruptedException e) {
            throw new ConnectionPoolException(e, "Cannot get free connection");
        }
        LOG.info("Free connections" + freeConnections.size() + " Used connections" + usedConnections);
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

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public static Logger getLOG() {
        return LOG;
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
