package com.epam.igor.electronicsshop.dao;

import com.epam.igor.electronicsshop.dao.entity.JDBCDaoFactory;
import com.epam.igor.electronicsshop.entity.BaseEntity;


/**
 * Dao factory for abstract transaction methods
 * @author Igor Lapin
*/
public abstract class DaoFactory implements AutoCloseable {
    public static final int JDBC = 0;

    public static DaoFactory getDaoFactory(int factoryType) throws DaoException {
        switch (factoryType) {
            case JDBC:
                return new JDBCDaoFactory();
            default:
                return new JDBCDaoFactory();
        }
    }

    public abstract <T extends BaseEntity> GenericDaoInterface<T> getDao(Class<T> clazz) throws DaoException;

    public abstract void close() throws DaoException;

    public abstract void startTransaction() throws DaoException;

    public abstract void commitTransaction() throws DaoException;

    public abstract void rollbackTransaction() throws DaoException;
}
