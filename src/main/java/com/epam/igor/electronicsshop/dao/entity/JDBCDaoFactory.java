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
    private static final String UNABLE_TO_GET_CONNECTION_FROM_THE_POOL = "Unable to get connection from the pool";
    private static final String CANNOT_BEGIN_TRANSACTION = "Cannot begin transaction";
    private static final String COMMIT_TRANSACTION = "Commit transaction";
    private static final String CANNOT_COMMIT_TRANSACTION = "Cannot commit transaction";
    private static final String ROLLBACK_TRANSACTION = "Rollback transaction";
    private static final String CANNOT_ROLLBACK_TRANSACTION = "Cannot rollback transaction";
    private static final String FORMAT = "%s.JDBC%sDao";
    private static final String DAO_OBJECT_FOR = "Dao object for ";
    private static final String NOT_FOUND = " not found.";
    private static final String CANNOT_CLOSE_CONNECTION = "Cannot close connection";
    private Connection connection = null;
    private ConnectionPool connectionPool;

    public JDBCDaoFactory() {
        connectionPool = ConnectionPool.getInstance();
        try {
            connection = connectionPool.getConnection();
        }
        catch (ConnectionPoolException e){
            LOG.error(UNABLE_TO_GET_CONNECTION_FROM_THE_POOL, e);
        }
    }



    public void startTransaction() throws DaoException{
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException(CANNOT_BEGIN_TRANSACTION, e);
        }
    }
    public void commitTransaction() throws DaoException{
        try {
            connection.commit();
            connection.setAutoCommit(true);
            LOG.debug(COMMIT_TRANSACTION);
        } catch (SQLException e) {
            throw new DaoException(CANNOT_COMMIT_TRANSACTION);
        }
    }

    @Override
    public void rollbackTransaction() throws DaoException {

    }

    public void rollBackTransaction() throws DaoException{
        try {
            connection.rollback();
            LOG.debug(ROLLBACK_TRANSACTION);
        } catch (SQLException e) {
            throw new DaoException(CANNOT_ROLLBACK_TRANSACTION);
        }
    }

    @Override
    public <T extends BaseEntity> GenericDaoInterface<T> getDao(Class<T> clazz) throws DaoException {
        JDBCAbstractDao<T> daoObject;
        try {
            String inputClassName = clazz.getSimpleName();
            String packageName = this.getClass().getPackage().getName();
            String resultClassName = String.format(FORMAT, packageName, inputClassName);
            daoObject = (JDBCAbstractDao<T>) Class.forName(resultClassName).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new DaoException(DAO_OBJECT_FOR + clazz + NOT_FOUND, e);
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
            throw new DaoException(CANNOT_CLOSE_CONNECTION, e);
        }
    }
}
