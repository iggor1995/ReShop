package com.epam.igor.electronicsshop.dao.entity;

import com.epam.igor.electronicsshop.dao.DaoException;
import com.epam.igor.electronicsshop.dao.DaoFactory;
import com.epam.igor.electronicsshop.dao.GenericDaoInterface;
import com.epam.igor.electronicsshop.entity.BaseEntity;
import com.epam.igor.electronicsshop.pool.ConnectionPool;
import com.epam.igor.electronicsshop.pool.ConnectionPoolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by User on 03.08.2017.
 */
public class JDBCDaoFactory extends DaoFactory{
    private static final Logger LOG = LoggerFactory.getLogger(DaoFactory.class);
    private Connection connection = null;
    private ConnectionPool connectionPool;

    public JDBCDaoFactory() {
        connectionPool = ConnectionPool.getInstance();
        try {
            connection = connectionPool.getConnection();
        }
        catch (ConnectionPoolException e){
            LOG.error("Unable to get connection from the pool", e);
        }
    }



    public void startTransaction() throws DaoException{
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException("Cannot begin transaction", e);
        }
    }
    public void commitTransaction() throws DaoException{
        try {
            connection.commit();
            connection.setAutoCommit(true);
            LOG.debug("Commit transaction");
        } catch (SQLException e) {
            throw new DaoException("Cannot commit transaction");
        }
    }

    @Override
    public void rollbackTransaction() throws DaoException {

    }

    public void rollBackTransaction() throws DaoException{
        try {
            connection.rollback();
            LOG.debug("Rollback transaction");
        } catch (SQLException e) {
            throw new DaoException("Cannot rollback transaction");
        }
    }

    @Override
    public <T extends BaseEntity> GenericDaoInterface<T> getDao(Class<T> clazz) throws DaoException {
        JDBCAbstractDao<T> daoObject;
        try {
            String inputClassName = clazz.getSimpleName();
            String packageName = this.getClass().getPackage().getName();
            String resultClassName = String.format("%s.JDBC%sDao", packageName, inputClassName);
            daoObject = (JDBCAbstractDao<T>) Class.forName(resultClassName).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new DaoException("Dao object for " + clazz + " not found.", e);
        }
        daoObject.setConnection(connection);
        return daoObject;
    }

    @Override
    public void close() throws DaoException {
        try {
            connectionPool.closeConnection(connection);
        }
        catch (ConnectionPoolException e){
            throw new DaoException("Cannot close connection", e);
        }
    }
}
